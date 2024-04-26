package com.workcv.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.workcv.model.Category;
import com.workcv.model.Company;
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
	@GetMapping("/employer/create-job")
	public String createJob(Model model) {
		// Lấy danh sách danh mục từ cơ sở dữ liệu
	    List<Category> categories = categoryService.getAllCategories();
	    model.addAttribute("categories", categories);
		// Lấy thông tin người dùng từ session để lấy thông tin công ty
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Job job = new Job ();
		
		model.addAttribute("job", job);
		model.addAttribute("username",currentUser.getFullName());
		model.addAttribute("role", currentUser.getRole());
		return "job-description-form";
	}
	//lưu job mới
	@PostMapping("/employer/save-job")
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
        // Lấy thông tin người dùng từ session để lấy thông tin công ty
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Company company = companyService.getCompanyByUserId(currentUser.getId());
        if (company != null) {
        	job.setCompany(company);
        	jobService.saveJob(job);
    		redirectAttributes.addFlashAttribute("saveJobSuccess", true);
    		return "redirect:/job/employer/create-job";
        }
        else {
        	redirectAttributes.addFlashAttribute("error", "Đăng tuyển thất bại! Chưa tạo hồ sơ công ty");
        	return "redirect:/job/employer/create-job";
        }

		
		
	}
	// hiển thị các job của công ty một công ty
	@GetMapping("/employer/manage-job")
	public String showManageJob(Model model) {
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("username",currentUser.getFullName());
		model.addAttribute("role", currentUser.getRole());
		//lấy danh sách các job của một công ty
		List<Job> jobs =  jobService.getJobsOfCompany(companyService.getCompanyByUserId(currentUser.getId()).getId());
		model.addAttribute("jobs", jobs);
		return "manage-job";
	}
	
	@GetMapping("/details/{id}")
	public String showDetailJd(Model model, @PathVariable("id")int id ) {
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("username",currentUser.getFullName());
		model.addAttribute("role", currentUser.getRole());
		Job job = jobService.getJobById(id);
		Optional<Company> company = companyService.getCompanyById(job.getCompany().getId());
		model.addAttribute("company",company.get());
		model.addAttribute("job", job);
		return "job-detail";
	}
	
	
	
	
	
	
}
