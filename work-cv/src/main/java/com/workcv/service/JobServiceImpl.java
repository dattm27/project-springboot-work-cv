package com.workcv.service;

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

	

}
