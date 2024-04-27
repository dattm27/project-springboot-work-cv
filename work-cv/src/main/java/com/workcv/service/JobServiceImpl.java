package com.workcv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workcv.model.Job;
import com.workcv.repository.JobRepository;
@Service
public class JobServiceImpl implements JobService {
	
	@Autowired
	private JobRepository jobRepository;

	@Override
	public Job saveJob(Job job) {
		// TODO Auto-generated method stub
		return jobRepository.save(job);
	}

	@Override
	public List<Job> getJobsOfCompany(int id) {
		return jobRepository.findJobByCompanyId(id);
	
	}

	@Override
	public Job getJobById(int id) {
		return jobRepository.findJobById(id);
	}

	@Override
	public void deleteJob(int jobId) {
		jobRepository.deleteById(jobId);
		
	}

	@Override
	public Job save(Job job) {
		Job savedJob = jobRepository.save(job);
		return savedJob;
	}

	

}
