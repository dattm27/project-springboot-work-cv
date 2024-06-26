package com.workcv.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workcv.model.Apply;

import com.workcv.repository.ApplyRepository;

@Service
public class ApplyServiceImpl implements ApplyService {

	@Autowired
	private ApplyRepository applyRepository;
	@Override
	public Apply save(Apply apply) {
		Apply savedApply = applyRepository.save(apply);
		return savedApply;
	}
	@Override
	public Apply getApply(int user_id, int job_id) {
		Apply apply = applyRepository.findByUserIdAndJobId(user_id, job_id);
		return apply;
	}
	@Override
	public List<Object[]> getApplicationDetailsByCompanyId(int companyId) {
		
		 return applyRepository.findApplicationDetailsByCompanyId(companyId);
	}
	@Override
	public Optional<Apply> getApplyById(int id) {
		Optional<Apply> apply = applyRepository.findById(id);
		return apply;
	}
	@Override
	public List<Object[]> getAppliedJobs(int user_id) {
		
		return applyRepository.findApplicationsByUserId(user_id);
	}

}
