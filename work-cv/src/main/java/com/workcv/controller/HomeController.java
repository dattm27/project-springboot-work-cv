package com.workcv.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
}
