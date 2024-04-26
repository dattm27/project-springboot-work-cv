package com.workcv.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.workcv.model.Category;
import com.workcv.model.CustomUserDetails;
import com.workcv.model.Job;
import com.workcv.service.CategoryService;
import com.workcv.service.CompanyService;
import com.workcv.service.JobService;

@Controller
@RequestMapping("/job")
public class JobController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private JobService jobService;
	@GetMapping("create-job")
	public String createJob(Model model) {
		// Lấy danh sách danh mục từ cơ sở dữ liệu
	    List<Category> categories = categoryService.getAllCategories();
	    model.addAttribute("categories", categories);
		// Lấy thông tin người dùng từ session để lấy thông tin công ty
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Job job = new Job ();
		job.setCompany(companyService.getCompanyByUserId(currentUser.getId()));
		model.addAttribute("job", job);
		model.addAttribute("username",currentUser.getFullName());
		model.addAttribute("role", currentUser.getRole());
		return "job-description-form";
	}
	//lưu job mới
	@PostMapping("/save-job")
	public String processCreateJob(@ModelAttribute("job") Job job, RedirectAttributes redirectAttributes, @RequestParam("deadline") String deadlineString) {
		 // Lấy ngày hiện tại
        Date currentDate = new Date();

        // Gán đối tượng Timestamp vào trường createdDate
        job.setCreatedDate(currentDate);
        // Định dạng ngày tháng "dd/MM/yyyy"
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        // Chuyển đổi chuỗi ngày thành đối tượng Date
        try {
			 Date deadline;
			deadline = formatter.parse(deadlineString);
			 job.setDeadline(deadline);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			  // Gán ngày tháng vào trường deadline của job
	       
		}
      

		jobService.saveJob(job);
		redirectAttributes.addFlashAttribute("saveJobSuccess", true);
		return "redirect:/job/create-job";
		
	}
}
