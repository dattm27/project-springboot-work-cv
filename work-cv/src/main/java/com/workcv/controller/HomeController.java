package com.workcv.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workcv.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeController {
	@Autowired
    private UserService userService; // Đây là một interface/service xử lý logic liên quan đến người dùng
	@GetMapping("/")
	public String showPage(Model model) {
		// Lấy đối tượng Authentication từ SecurityContextHolder
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    // Kiểm tra xem người dùng đã đăng nhập hay chưa
	    if (authentication != null && authentication.isAuthenticated()) {
	        // Lấy tên người dùng từ userService dựa vào username lấy được đối tượng Authentication
	        String username = userService.getFullnameByEmail(authentication.getName());
	        //lấy vai trò người dùng để hiển thị các chức năng tương ứng
	        String role = userService.getRoleByEmail(authentication.getName());
	        //thêm vào model các thuộc tính tên người dùng và vai trò
	        model.addAttribute("username", username);
	        model.addAttribute("role", role);
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
	
	
}
