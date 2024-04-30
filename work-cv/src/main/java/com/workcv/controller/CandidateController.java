package com.workcv.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.FileSystemResource;

import com.workcv.model.Apply;
import com.workcv.model.CV;
import com.workcv.model.CustomUserDetails;
import com.workcv.model.User;
import com.workcv.service.ApplyService;
import com.workcv.service.CvService;
import com.workcv.service.UserService;

@Controller
@RequestMapping("/candidate")
public class CandidateController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private CvService cvService;
	@Autowired
	private ApplyService applyService;

	public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/webapp/cv";

	@PostMapping("/save-cv")
	public String saveCV(@RequestParam("cv") MultipartFile file, RedirectAttributes redirectAttributes)
			throws IOException {
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User user = userService.getUserByEmail(currentUser.getUsername());
		// lay cv moi duoc chon
		String originalFilename = file.getOriginalFilename();
		// ghi anh vao thu muc duoc luu tru
		Path fileNameAndPath = Paths.get(uploadDirectory, originalFilename);
		// Ghi file vao thu muc
		Files.write(fileNameAndPath, file.getBytes());

		// xoa CV cu neu co
		CV oldCv = user.getCv();
		if (oldCv != null) {
			// thuc hien xoa anh
			String oldCvFilename = oldCv.getFilename();
			Path oldCvPath = Paths.get(uploadDirectory, oldCvFilename);
			Files.deleteIfExists(oldCvPath);
			oldCv.setFilename(originalFilename);
			currentUser.getUser().setCv(oldCv);
			//Cập nhật lại trong cơ sở dữ liệu
			cvService.save(oldCv);

		} else {
			// Tao doi tuong cv moi
			CV cv = new CV();
			cv.setFilename(originalFilename);
			cv.setUser(user);
			
			cvService.save(cv);
			currentUser.getUser().setCv(cv);
			
		}

		redirectAttributes.addFlashAttribute("msg", "Cập nhật CV thành công");
		return "redirect:/user/profile";
	}
	
	//Nạp file CV của ứng viên lên
	@GetMapping("cv/{id}")
	public ResponseEntity<Resource> getCV(@PathVariable("id") int id) throws IOException {
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		//Get the cv from user
		Path cvPath = Paths.get(uploadDirectory, currentUser.getUser().getCv().getFilename());
		//Fetching the image from that particular path
		Resource resource = new FileSystemResource(cvPath.toFile());
		String contentType = Files.probeContentType(cvPath);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
	}
	
	@GetMapping("/apply/{id}")
	public String getApplyForm(@PathVariable("id") int id, Model model) {
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Apply applied= applyService.getApply( currentUser.getId(), id);
		
		if (applied == null) {
			model.addAttribute("id", id);
			CV cv = currentUser.getUser().getCv();
			model.addAttribute("cv", cv);
			return "apply-form";
		}
		else {
			model.addAttribute("error", "Bạn đã ứng tuyển công việc này");
			return "custom-error";
		}
		
	}
}
