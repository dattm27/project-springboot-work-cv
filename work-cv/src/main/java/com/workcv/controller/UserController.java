package com.workcv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.workcv.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@GetMapping("/list")
	public String showList(Model model) {
		model.addAttribute("listUsers", userService.getAllUsers() );
		return "list";
	}
	@GetMapping("/signup")
	//hiển thị form đăng ký tài khoản
	public  String showSignUpForm () {
		return "sign-up-form";
	}
	
	@PostMapping("/signup")
	public String processSignup(@RequestParam("email") String email, @RequestParam("full-name")String fullName, @RequestParam("password") String password, @RequestParam("role") String role , Model model) {
		//kiểm tra email trùng lặp
		 boolean success = userService.registerUser(fullName, email, password, role);
		if(!success) {
			//nếu đã tồn tại email như thế
			model.addAttribute("error", true);
			return "sign-up-form";
		}
		else {
			//dang ky thanh cong
			model.addAttribute("sign_up_success", true);
			return "login-form";
		}
	}
}
