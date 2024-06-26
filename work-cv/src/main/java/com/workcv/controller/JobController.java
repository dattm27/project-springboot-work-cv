package com.workcv.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.workcv.model.FollowingCompany;
import com.workcv.model.Job;
import com.workcv.model.SavedJob;
import com.workcv.service.CategoryService;
import com.workcv.service.CompanyService;
import com.workcv.service.JobService;
import com.workcv.service.UserService;

@Controller
@RequestMapping("/job")
public class JobController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private JobService jobService;
	@Autowired
	private UserService userService;

	@GetMapping("/employer/create-job")
	public String createJob(Model model) {
		// Lấy danh sách danh mục từ cơ sở dữ liệu
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);
		// Lấy thông tin người dùng từ session để lấy thông tin công ty
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Job job = new Job();

		model.addAttribute("job", job);
		model.addAttribute("username", currentUser.getFullName());
		model.addAttribute("role", currentUser.getRole());
		// thêm lệnh cho biết đây là tạo mới job -> phân biệt với update
		model.addAttribute("command", "create");
		return "job-description-form";
	}

	// lưu job (thêm job mới + cập nhật job cũ)
	@PostMapping("/employer/save-job")
	public String processCreateJob(@ModelAttribute("job") Job job, RedirectAttributes redirectAttributes,
			@RequestParam("deadline") String deadlineString, @RequestParam("command") String command,
			@RequestParam(value = "newCategory", required = false) String newCategory) {
		// Định dạng ngày tháng "dd/MM/yyyy"
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		// Xử lý xem có phải danh mục mới không
		if (job.getCategory().getId() == 2) { // Id = 2 trong cơ sở dữ liệu chính là khác
			// thêm category mới
			Category category = new Category();
			category.setName(newCategory);
			// lưu category mới vào cơ sở dữ liệu
			categoryService.save(category);
			// đắt category mới cho job
			job.setCategory(category);
		}
		// đây là khi form gửi tới là tạo mới job
		if (command.equalsIgnoreCase("create")) {
			// Lấy ngày hiện tại
			Date currentDate = new Date();
			// Gán đối tượng Timestamp vào trường createdDate
			job.setCreatedDate(currentDate);

			try {
				// Chuyển đổi chuỗi ngày thành đối tượng Date
				Date deadline;
				deadline = formatter.parse(deadlineString);
				job.setDeadline(deadline);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// Gán ngày tháng vào trường deadline của job

			}
			// Lấy thông tin người dùng từ session để lấy thông tin công ty
			CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Company company = companyService.getCompanyByUserId(currentUser.getId());
			if (company != null) {
				job.setCompany(company);
				jobService.saveJob(job);
				redirectAttributes.addFlashAttribute("saveJobSuccess", true);
				return "redirect:/job/employer/create-job";
			} else {
				redirectAttributes.addFlashAttribute("error", "Đăng tuyển thất bại! Chưa tạo hồ sơ công ty");
				return "redirect:/job/employer/create-job";
			}
		}
		// khi form gửi tới là form cập nhật
		if (command.equalsIgnoreCase("update")) {
			Date deadline;
			// lấy đối tượng job cũ dể cập nhật
			System.out.print(job.getId());
			Job oldJob = jobService.getJobById(job.getId());

			try {

				deadline = formatter.parse(deadlineString);
				// đặt deadline mới cho job
				oldJob.setDeadline(deadline);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// Gán ngày tháng vào trường deadline của job

			}

			// cập nhật các giá trị mới cho job
			oldJob.setAddress(job.getAddress());
			oldJob.setDescription(job.getDescription());
			oldJob.setExperience(job.getExperience());
			oldJob.setSalary(job.getSalary());
			oldJob.setCategory(job.getCategory());
			oldJob.setQuantity(job.getQuantity());
			oldJob.setStatus(job.getStatus());

			// lưu lại thông tin
			jobService.save(oldJob);

		}
		redirectAttributes.addFlashAttribute("saveJobSuccess", true);
		return "redirect:/job/employer/edit-jd/" + job.getId();

	}

	// hiển thị các job của công ty một công ty
	@GetMapping("/employer/manage-job")
	public String showManageJob(Model model) {
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		model.addAttribute("username", currentUser.getFullName());
		model.addAttribute("role", currentUser.getRole());
		// lấy danh sách các job của một công ty
		List<Job> jobs = jobService.getJobsOfCompany(currentUser.getUser().getCompany().getId());
		model.addAttribute("jobs", jobs);
		return "manage-job";
	}

	// khi employer nhấn cập nhật job -> Hiển thị ra form thông tin của job với các
	// giá trị sẵn có
	@GetMapping("employer/edit-jd/{id}")
	public String editJob(@PathVariable("id") int id, Model model) {
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		model.addAttribute("username", currentUser.getFullName());
		model.addAttribute("role", currentUser.getRole());
		Job job = jobService.getJobById(id);
		model.addAttribute("job", job);
		// Chuyển đổi định dạng ngày tháng thành "dd/MM/yyyy"
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDeadline = formatter.format(job.getDeadline());
		// chuyen deadline vao
		model.addAttribute("deadline", formattedDeadline);
		// Lấy danh sách danh mục từ cơ sở dữ liệu và truyền vào form
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);
		// Thêm lệnh cho biết form này là form cập nhật
		model.addAttribute("command", "update");
		return "job-description-form";
	}

	@DeleteMapping("/employer/deleteJob/{jobId}")
	public String deleteJob(@PathVariable("jobId") int jobId) {
		jobService.deleteJob(jobId);
		System.out.print(jobId);
		return "manage-job";

	}

	@GetMapping("/details/{id}")
	public String showDetailJd(Model model, @PathVariable("id") int id) {
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		model.addAttribute("username", currentUser.getFullName());
		model.addAttribute("role", currentUser.getRole());
		Job job = jobService.getJobById(id);
		// xem người dùng save job này chưa
		boolean saved = false;
		for (SavedJob savedJob : currentUser.getUser().getSavedJobs()) {
			if (savedJob.getJob().getId() == id) {
				saved = true;
				break;
			}
		}
		model.addAttribute("saved", saved);

		// tăng view
		int view = job.getView();
		job.setView(view + 1);
		jobService.save(job);
		// Thêm thông tin công ty của job này
		Optional<Company> company = companyService.getCompanyById(job.getCompany().getId());
		model.addAttribute("company", company.get());
		model.addAttribute("job", job);

		// xem người dùng theo dõi công ty này chưa
		boolean following = false;
		FollowingCompany followingCompany = userService.getFollowingCompanyByUserIdAndCompanyId(currentUser.getUser(),
				companyService.getCompanyById(company.get().getId()).get());
		if (followingCompany != null) {
			following = true;
		}
		model.addAttribute("following", following);

		return "job-detail";
	}

	// Hiển thị danh sách toàn bộ công việc đang tuyển trong hệ thống
	@GetMapping("/list")
	public String showJobList(Model model) {
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		model.addAttribute("role", currentUser.getRole());
		model.addAttribute("username", currentUser.getFullName());
		// list trendy jobs theo chiều số lượng người ứng tuyển giảm rồi
		List<Job> jobs = jobService.getAvailableJobs();
		// thêm list danh mục nổi bật (có nhiều jobs)
		List<Object[]> categories = categoryService.getTrendyCategories();
		// them list cong ty noi bat
		List<Object[]> companies = companyService.getTopCompanies();
		model.addAttribute("jobs", jobs);
		model.addAttribute("categories", categories);
		model.addAttribute("companies", companies);
		return "job-list";
	}
}
