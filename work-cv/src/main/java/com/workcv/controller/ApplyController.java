package com.workcv.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.workcv.model.Apply;
import com.workcv.model.CustomUserDetails;
import com.workcv.service.ApplyService;
import com.workcv.service.JobService;
import com.workcv.service.UserService;

@Controller
@RequestMapping("/apply")
public class ApplyController {
	@Autowired
	private ApplyService applyService;
	@Autowired
	private JobService jobService;

	@PostMapping("/save-apply/{job_id}")
	public String apply( @PathVariable("job_id") int job_id ){
		// Lấy thông tin người dùng từ session để lấy thông tin công ty
				CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
		Apply apply = new Apply();
		apply.setJob(jobService.getJobById(job_id));
		apply.setUser(currentUser.getUser());
		apply.setStatus("Chờ xét duyệt");
		Date currentDate = new Date();
		apply.setCreatedAt(currentDate);
		System.out.print("Apply" + apply.getUser().getEmail() + "Job: " +apply.getJob().getTitle());
		applyService.save(apply);
		return "job-list";
	}

}
