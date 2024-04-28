package com.workcv.service;

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

}
