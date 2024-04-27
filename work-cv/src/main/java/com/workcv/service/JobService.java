package com.workcv.service;

import java.util.List;

import com.workcv.model.Job;

public interface JobService {
	
	Job saveJob(Job job);
	List<Job> getJobsOfCompany(int id);
	Job getJobById(int id);
	void deleteJob(int jobId);
	//dùng khi cập nhật thông tin jd
	Job save(Job job);
}
