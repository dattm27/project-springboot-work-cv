package com.workcv.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.workcv.model.User;
import com.workcv.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {
	@Autowired
    private UserService userService; // Đây là một interface/service xử lý logic liên quan đến người dùng
	//process đăng nhập được config trong security

	@GetMapping("/signin")
	public String showLoginForm() {
		return "login-form";
	}

	@PostMapping("/login")
	public String processLogin(HttpServletRequest request, @RequestParam("email") String email, @RequestParam("password") String password, Model model, RedirectAttributes redirectAttributes) {
	    if (userService.authenticate(email, password)) {
	        // Đăng nhập thành công, thêm thông tin người dùng vào Session
	        HttpSession session = request.getSession();
	        session.setAttribute("loggedInUser", email);
	        return "redirect:/";
	    } else {
	    	// Đăng nhập thất bại, chuyển hướng về trang đăng nhập và hiển thị thông báo lỗi
	        model.addAttribute("error", "Tài khoản hoặc mật khẩu không đúng. Vui lòng thử lại");
	        return "login-form";
	    }
		  
	}
	
	

}
