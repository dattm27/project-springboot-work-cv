package com.workcv.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.workcv.model.Company;
import com.workcv.model.CustomUserDetails;
import com.workcv.service.CompanyService;
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
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/webapp/images";
	
	@GetMapping("/company-profile")
	public String editProfile(HttpServletRequest request, Model model ) {
		//tạo đối tượng company mới
		Company company = new Company ();
		
		//thêm vào attribute vào model
		model.addAttribute("company" ,company);
	    //Thêm thông tin user, role vào
		HttpSession session = request.getSession();
	    String username = (String) session.getAttribute("username");
		String role = (String) session.getAttribute("role");
		model.addAttribute("username", username);
		model.addAttribute("role", role);
		return "employer-profile";
	}
	@PostMapping("/save-company")
	// Lưu thông tin của company vào database
	public String saveCompany(@ModelAttribute("company") Company company, @RequestParam("image") MultipartFile file, Authentication authentication) throws IOException {
	    
		if (file.isEmpty()) {
	        // Xử lý lỗi: không có tệp ảnh được chọn
	        throw new RuntimeException("No image file selected");
	    }

	    String originalFilename = file.getOriginalFilename();
	    // Kiểm tra định dạng tệp ảnh (nếu cần)
	    String[] allowedFileTypes = {"image/jpeg", "image/png", "image/gif"}; // Thêm định dạng tệp hình ảnh được phép
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
	        // Thực hiện lưu thông tin công ty vào cơ sở dữ liệu
	        // Ví dụ:
	        //Lấy xem user nào đang thực thực hiện cập nhật thông tin công ty
	        CustomUserDetails currentUser = ( CustomUserDetails) authentication.getPrincipal();
	        //Thêm user đó vào company
	        company.setUser(userService.getUserByEmail(currentUser.getUsername()));
	        Company savedCompany = companyService.save(company);
	        
	        return "employer-profile";
	    } catch (IOException e) {
	        // Xử lý lỗi: không thể ghi tệp ảnh vào thư mục lưu trữ
	        throw new RuntimeException("Failed to save image file", e);
	    }
	}
}
