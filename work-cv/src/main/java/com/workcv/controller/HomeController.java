package com.workcv.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeController {
	@GetMapping("/")
	public String showPage() {
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
		// Lấy đối tượng Authentication từ SecurityContextHolder
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    // Kiểm tra xem người dùng đã đăng nhập hay chưa
	    if (authentication != null && authentication.isAuthenticated()) {
	        // Lấy tên người dùng từ đối tượng Authentication
	        String username = authentication.getName();
	        // Bạn có thể sử dụng biến username ở đây để thực hiện các hành động khác
	        System.out.println("User logged in: " + username);
	    }
		return  "login-form" ;
	}
	@GetMapping("/signout")
	public String signout(HttpServletRequest request, HttpServletResponse response) {
		 // Xóa session của người dùng
        request.getSession().invalidate();
		return  "logout" ;
	}
	
	
}
