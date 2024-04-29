package com.workcv.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.workcv.model.Apply;
import com.workcv.model.CV;
import com.workcv.model.CustomUserDetails;
import com.workcv.service.ApplyService;
import com.workcv.service.CvService;
import com.workcv.service.JobService;
import com.workcv.service.UserService;

@Controller
@RequestMapping("/apply")
public class ApplyController {
	@Autowired
	private ApplyService applyService;
	@Autowired
	private JobService jobService;
	@Autowired
	private CvService cvService;
	public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/webapp/cv";
	@PostMapping("/save-apply/{job_id}")
	public String apply( @PathVariable("job_id") int job_id, @RequestParam("cv") MultipartFile file, @RequestParam("note") String note ,  RedirectAttributes redirectAttributes) throws IOException{
		// Lấy thông tin người dùng từ session để lấy thông tin công ty
				CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
		Apply apply = new Apply();
		apply.setJob(jobService.getJobById(job_id));
		apply.setUser(currentUser.getUser());
		apply.setStatus("Chờ xét duyệt");
		Date currentDate = new Date();
		apply.setCreatedAt(currentDate);
		apply.setNote(note);
		//Kiểm tra xem có CV mới gửi không
		if (file != null && !file.isEmpty()) { // có file cv
			// lay cv moi duoc chon
			String originalFilename = file.getOriginalFilename();
			// ghi anh vao thu muc duoc luu tru
			Path fileNameAndPath = Paths.get(uploadDirectory, originalFilename);
			// Ghi file vao thu muc
			Files.write(fileNameAndPath, file.getBytes());
			CV cv = new CV ();
			cv.setFilename(originalFilename);
			
			cvService.save(cv);
			
			apply.setCv(cv);
			
		}
		applyService.save(apply);
		redirectAttributes.addAttribute("username", currentUser.getFullName());
		redirectAttributes.addAttribute("role", currentUser.getRole());
		redirectAttributes.addFlashAttribute("msg", "Ứng tuyển thành công");
		return "redirect:/job/details/" + job_id;
	}

}
