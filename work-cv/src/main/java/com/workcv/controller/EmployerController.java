package com.workcv.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.workcv.model.Apply;

import com.workcv.model.Company;
import com.workcv.model.CustomUserDetails;
import com.workcv.model.Job;
import com.workcv.model.User;
import com.workcv.service.ApplyService;
import com.workcv.service.CompanyService;
import com.workcv.service.CvService;
import com.workcv.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/employer")
public class EmployerController {
	@Autowired
	private CompanyService companyService;
	@Autowired
	private UserService userService;
	@Autowired
	private ApplyService applyService;
	@Autowired
	private CvService cvService;
	public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/webapp/employer";

	@GetMapping("/company-profile")
	public String editProfile(HttpServletRequest request, Model model) throws Exception {
		// Kiểm tra xem flash scope có chứa attribute "updateSuccess" không - tức là lúc
		// này vừa mới cập nhật xong là được redirect
		if (request.getAttribute("updateSuccess") != null) {
			// Nếu có, thêm giá trị của attribute vào model
			model.addAttribute("updateSuccess", request.getAttribute("updateSuccess"));
			System.out.print("OK");
		}

		// Lấy thông tin người dùng từ session để lấy thông tin công ty
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int userId = currentUser.getId();
		// Lấy thông tin công ty từ endpoint /data/{user_id}
		Company existingCompany = getCompanyByUserId(userId).getBody();
		if (existingCompany != null) { // nếu đã có thông tin công ty rồi
			model.addAttribute("company", existingCompany);
			//nếu công ty có sẵn ảnh 
			if(existingCompany.getLogo()!= null) {
				Resource imageResource = getProfileImage(userId).getBody();
				
				 model.addAttribute("imageResource", imageResource);
			}
			
		} else {// nếu chưa tạo thông tin công ty
			// tạo đối tượng company mới
			Company company = new Company();

			// thêm vào attribute vào model
			model.addAttribute("company", company);
		}
		// Thêm thông tin user, role vào
		String username = (String) currentUser.getFullName(); // thêm tên người dùng vào để hiển thị trên navbar
		String role = (String) currentUser.getRole();
		model.addAttribute("username", username);
		model.addAttribute("role", role);
		return "company-profile";
	}

	@PostMapping("/save-company")
	// Lưu thông tin của company vào database
	public String saveCompany(@ModelAttribute("company") Company company, @RequestParam("image") MultipartFile file,
			Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
		// Kiểm tra xem thông tin công ty cũ đã tồn tại trong cơ sở dữ liệu hay chưa

		CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();// Lấy xem user nào đang thực
																							// thực hiện cập nhật thông
																							// tin công ty
		int userId = currentUser.getId();
		// Lấy thông tin công ty từ endpoint /data/{user_id}

		Company existingCompany = getCompanyByUserId(userId).getBody();

		if (existingCompany != null) {
			// Cập nhật thông tin công ty

			// Kiểm tra xem người dùng có thay đổi ảnh không
			if (file != null && !file.isEmpty()) {
				// Xóa ảnh cũ nếu có
				String oldLogo = existingCompany.getLogo();
				if (oldLogo != null && !oldLogo.isEmpty()) {
					Path oldLogoPath = Paths.get(uploadDirectory, oldLogo);
					Files.deleteIfExists(oldLogoPath);
				}

				// Ảnh mới được chọn
				String originalFilename = file.getOriginalFilename();
				// Kiểm tra định dạng tệp ảnh (nếu cần)
				String[] allowedFileTypes = { "image/jpeg", "image/png", "image/gif" }; // Thêm định dạng tệp hình ảnh
																						// được phép
				if (!Arrays.asList(allowedFileTypes).contains(file.getContentType())) {
					// Xử lý lỗi: định dạng tệp không hợp lệ
					throw new RuntimeException("Invalid file format. Only JPEG, PNG, and GIF files are allowed");
				}
				// Ghi tệp ảnh vào thư mục lưu trữ
				Path fileNameAndPath = Paths.get(uploadDirectory, originalFilename);
				Files.write(fileNameAndPath, file.getBytes());
				// Cập nhật tên tệp ảnh trong đối tượng công ty
				existingCompany.setLogo(originalFilename);
			}

			// Cập nhật thông tin công ty (tên, địa chỉ, email, ...)
			existingCompany.setName(company.getName());
			existingCompany.setAddress(company.getAddress());
			existingCompany.setEmail(company.getEmail());
			existingCompany.setPhoneNumber(company.getPhoneNumber());
			existingCompany.setDescription(company.getDescription());
			existingCompany.setStatus(company.getStatus());

			// Thực hiện lưu thông tin công ty vào cơ sở dữ liệu
			companyService.save(existingCompany);
		} else {
			// Trường hợp chưa có sẵn thông tin công ty trong cơ sở dữ liệu
			if (!file.isEmpty()) {
				String originalFilename = file.getOriginalFilename();
				// Kiểm tra định dạng tệp ảnh (nếu cần)
				String[] allowedFileTypes = { "image/jpeg", "image/png", "image/gif", "image/jpg" }; // Thêm định dạng
																										// tệp hình ảnh
																										// được
				// phép
				if (!Arrays.asList(allowedFileTypes).contains(file.getContentType())) {
					// Xử lý lỗi: định dạng tệp không hợp lệ
					throw new RuntimeException("Invalid file format. Only JPEG, PNG, and GIF files are allowed");
				}

				Path fileNameAndPath = Paths.get(uploadDirectory, originalFilename);
				try {
					// Ghi tệp ảnh vào thư mục lưu trữ
					Files.write(fileNameAndPath, file.getBytes());
					// Cập nhật tên tệp ảnh trong đối tượng company
					company.setLogo(originalFilename);

				} catch (IOException e) {
					// Xử lý lỗi: không thể ghi tệp ảnh vào thư mục lưu trữ
					throw new RuntimeException("Failed to save image file", e);
				}
			}

			// Thực hiện lưu thông tin công ty vào cơ sở dữ liệu
			// Ví dụ:
			// Lấy xem user nào đang thực thực hiện cập nhật thông tin công ty
			currentUser = (CustomUserDetails) authentication.getPrincipal();
			// Thêm user đó vào company
			company.setUser(userService.getUserByEmail(currentUser.getUsername()));
			Company savedCompany = companyService.save(company);

		}
		// Thêm thông điệp vào RedirectAttributes
		redirectAttributes.addFlashAttribute("updateSuccess", true);
		System.out.print("updateSuccess set at save-company");
		return "redirect:/employer/company-profile";
	}

	// Fetching data by user_id
	@GetMapping("/data/{user_id}")
	public ResponseEntity<Company> getCompanyByUserId(@PathVariable int user_id) {
		Company company = companyService.getCompanyByUserId(user_id);
		return ResponseEntity.ok().body(company);
	}

	// đã cop ra home controller
	// Fetching the image of a particular company
	@GetMapping("/profileImage/{user_id}")
	public ResponseEntity<Resource> getProfileImage(@PathVariable("user_id") int user_id) throws IOException {
		Company company = companyService.getCompanyByUserId(user_id);
		// Get the image from the company object
		Path imagePath = Paths.get(uploadDirectory, company.getLogo());

		// Fetching the image from that particular path
		Resource resource = new FileSystemResource(imagePath.toFile());
		;
		String contentType = Files.probeContentType(imagePath);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
	}

	// lấy danh sách các ứng viên của công ty mình
	@GetMapping("/applicants-list")
	public String showApplications(Model model) throws IOException {
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		model.addAttribute("username", currentUser.getFullName());
		model.addAttribute("role", currentUser.getRole());
		int companyId = currentUser.getUser().getCompany().getId();
		

		List<Object[]> applicationDetails = applyService.getApplicationDetailsByCompanyId(companyId);

		// In ra thông tin applicationDetails
		for (Object[] details : applicationDetails) {
			for (Object detail : details) {
				System.out.print(detail + " ");
			}
			System.out.println();
		}

		List<User> users = userService.getAllUsers();
		model.addAttribute("users", users);

		model.addAttribute("applications", applicationDetails);

		return "manage-applications"; // Thay thế bằng tên view của bạn
	}

	// Nạp file CV của ứng viên lên
	@GetMapping("cv/{id}")
	public ResponseEntity<Resource> getCV(@PathVariable("id") int id) throws IOException {
		String filename = cvService.getCVById(id).get().getFilename();
		// Get the cv from user
		Path cvPath = Paths.get(System.getProperty("user.dir") + "/src/main/webapp/cv", filename);
		// Fetching the image from that particular path
		Resource resource = new FileSystemResource(cvPath.toFile());
		String contentType = Files.probeContentType(cvPath);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
	}

//	duyệt ứng viên
	@PostMapping("accept/{apply_id}")
	public String acceptApplicant(@PathVariable("apply_id") int id, RedirectAttributes redirectAttributes)
			throws IOException {
		Apply apply = applyService.getApplyById(id).get();
		apply.setStatus("Đã duyệt");
		applyService.save(apply);

		return "redirect:/employer/applicants-list";
	}

//	duyệt ứng viên
	@PostMapping("decline/{apply_id}")
	public String declineApplicant(@PathVariable("apply_id") int id, RedirectAttributes redirectAttributes)
			throws IOException {
		Apply apply = applyService.getApplyById(id).get();
		apply.setStatus("Đã từ chối");
		applyService.save(apply);

		return "redirect:/employer/applicants-list";
	}
	
	
}
