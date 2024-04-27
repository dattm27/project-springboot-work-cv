package com.workcv.controller;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.workcv.model.Company;
import com.workcv.service.CompanyService;
import com.workcv.service.JobService;
import com.workcv.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
    private CompanyService companyService;
	@Autowired
    private JobService jobService;
	@Autowired
    private UserService userService;// Đây là một interface/service xử lý logic liên quan đến người dùng
	@GetMapping("/")
	public String showPage(HttpServletRequest request, Model model) {
		// Lấy đối tượng Authentication từ SecurityContextHolder
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    // Kiểm tra xem người dùng đã đăng nhập hay chưa
	    if (authentication != null && authentication.isAuthenticated()) {
	        // Lấy tên người dùng từ userService dựa vào username lấy được đối tượng Authentication
	        String username = userService.getFullnameByEmail(authentication.getName());
	        //lấy vai trò người dùng để hiển thị các chức năng tương ứng
	        String role = userService.getRoleByEmail(authentication.getName());
	        //chỉ cần chọn một trong hai cách để truyền đi username password
	        //thêm vào model các thuộc tính tên người dùng và vai trò
	        
	        
	        model.addAttribute("username", username);
	        model.addAttribute("role", role);
	        
	        //thêm thông tin người dùng vào phiên đăng nhập
	     
	        HttpSession session = request.getSession();
	        session.setAttribute("username", username);
	        session.setAttribute("role", role);
	    }
		return "index";
	}
	@GetMapping("/candidate")
	public String candidate() {
		return  "This is candidate" ;
	}
	@GetMapping("/employer")
	public String employer() {
		return  "This is employer" ;
	}
	@GetMapping("/signin")
	public String signin() {
		
		return  "login-form" ;
	}
	@GetMapping("/signout")
	public String signout(HttpServletRequest request, HttpServletResponse response) {
		 // Xóa session của người dùng
        request.getSession().invalidate();
		return  "logout" ;
	}
		// Fetching the image of a particular company
		@GetMapping("/profileImage/{id}")
		public ResponseEntity<Resource> getProfileImage(@PathVariable("id") int id) throws IOException {
			Company company = companyService.getCompanyById(id).get();
			// Get the image from the company object
			String uploadDirectory = System.getProperty("user.dir") + "/src/main/webapp/employer";
			Path imagePath = Paths.get(uploadDirectory, company.getLogo());
			
			// Fetching the image from that particular path
			Resource resource = new FileSystemResource(imagePath.toFile());
			;
			String contentType = Files.probeContentType(imagePath);
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
		}
		
	
}
