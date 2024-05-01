package com.workcv.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.workcv.model.CustomUserDetails;
import com.workcv.model.User;
import com.workcv.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/webapp/user"; // đường dẫn lưu
																										// ảnh avatar về

	@GetMapping("/list")
	public String showList(Model model) {
		model.addAttribute("listUsers", userService.getAllUsers());
		return "list";
	}

	@GetMapping("/signup")
	// hiển thị form đăng ký tài khoản
	public String showSignUpForm() {
		return "sign-up-form";
	}

	@PostMapping("/signup")
	public String processSignup(@RequestParam("email") String email, @RequestParam("full-name") String fullName,
			@RequestParam("password") String password, @RequestParam("role") String role, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException, MessagingException {
		// Mã hóa mật khẩu
		String encodedPassword = passwordEncoder.encode(password);
		// kiểm tra email trùng lặp
		User user = userService.registerUser(fullName, email, encodedPassword, role);
		//tạo link để gửi người dùng bấm vào xác thực tài khoản (link này sau đó sẽ được thêm mã xác thực vào)
		String siteURL = request.getRequestURL().toString();
		userService.sendVerificationCode(user,  siteURL);
		if (user == null) {
			// nếu đã tồn tại email như thế
			model.addAttribute("error", true);
			return "sign-up-form";
		} else {
			
			// dang ky thanh cong
//			model.addAttribute("sign_up_success", true);
			model.addAttribute("msg", "Đăng ký thành công! Kiểm tra email để xác thực tài khoản");
			return "login-form";
		}
	}
	
	//xác thực người dùng
	@GetMapping("/signup/verify")
	public String verifyAccount(@RequestParam("code") String code, Model model) {
		
		boolean verify = userService.verify(code);
		if (verify) {
			String msg = "Xác thực thành công! Vui lòng đăng nhập!";
			model.addAttribute("msg", msg);
		}
		else {
			String error = "Gặp lỗi khi xác thực";
			model.addAttribute("error",error);	
		}
		return "login-form";
	}
	

//	Chỉnh sửa thông tin cá nhân của người dùng
	@GetMapping("profile")
	public String editProfile(HttpServletRequest request, Model model) throws IOException {

		// đã đăng nhập chắc chắn có tài khoản trong cơ sở dữ liệu -> lấy user đó lên
		// rồi truyền vào
		// kiểm tra xem ai đang đăng nhập
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		// Lấy đối tượng user đang đăng nhập
		User user = userService.getUserByEmail(currentUser.getUsername());
		if (user.getImage()!= null && !user.getImage().isEmpty()) {
			Resource imageResource = getAvatar(user.getEmail()).getBody();
			model.addAttribute("imageResource", imageResource);
		}
		
	
		model.addAttribute("user", user);
		model.addAttribute("username", user.getFullName());
		model.addAttribute("role", user.getRole());
		if (user.getCv()!= null) model.addAttribute("cv", user.getCv());
		return "profile";
	}

	// Xử lý thông tin cá nhân người khi dùng cập nhật
	@PostMapping("save-info")
	public String saveProfile(@ModelAttribute("user") User updatedUser, @RequestParam("avatar") MultipartFile file,
			Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
		//Lấy ra người dùng hiện tại đang thực hiện cập nhật dữ liệu
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		User user = userService.getUserByEmail(currentUser.getUsername());
		
		// kiểm tra xem người dùng có tải lên ảnh không?
		if (file != null && !file.isEmpty()) { // có ảnh tải lên
			// xoá ảnh cũ nếu có
			String oldImg = user.getImage();
			if (oldImg != null && !oldImg.isEmpty()) {
				Path oldImgPath = Paths.get(uploadDirectory, oldImg);
				Files.deleteIfExists(oldImgPath);
			}
			// ảnh mới được chọn
			String originalFilename = file.getOriginalFilename();
			// kiểm tra định dạng ảnh
			String[] allowedFileTypes = { "image/jpeg", "image/png", "image/gif" }; // Thêm định dạng tệp hình ảnh
			// được phép
			if (!Arrays.asList(allowedFileTypes).contains(file.getContentType())) {
			// Xử lý lỗi: định dạng tệp không hợp lệ
				throw new RuntimeException("Invalid file format. Only JPEG, PNG, and GIF files are allowed");
			}
			//ghi ảnh vào thư mục lưu trữ 
			Path fileNameAndPath = Paths.get(uploadDirectory, originalFilename);
			Files.write(fileNameAndPath, file.getBytes());
			//cập nhật ảnh 
			user.setImage(originalFilename);
			
		}
		//cập nhật các thông tn khác
		user.setAddress(updatedUser.getAddress());
		user.setDescription(updatedUser.getDescription());
		
		user.setFullName(updatedUser.getFullName());
		user.setDescription(updatedUser.getDescription());
		user.setPhoneNumber(updatedUser.getPhoneNumber());
		redirectAttributes.addFlashAttribute("msg", "Cập nhật thông tin cá nhân thành công");
		//Lưu thông tin người dùng mới cập nhật vào cơ sở dữ liệu
		userService.save(user);
		return "redirect:/user/profile";
	}
	//Nạp ảnh đại diện của người dùng lên 
	@GetMapping("/avatar/{email}")
	public ResponseEntity<Resource> getAvatar(@PathVariable String email) throws IOException{
		User user = userService.getUserByEmail(email);
		//Get the image from the user
		Path imagePath = Paths.get(uploadDirectory, user.getImage());
		
		//Fetching the image from that particular path
		Resource resource = new FileSystemResource(imagePath.toFile());
		String contentType = Files.probeContentType(imagePath);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
	}
}
