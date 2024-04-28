package com.workcv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workcv.model.CV;
import com.workcv.repository.CvRepository;
@Service
public class CvServiceImpl implements CvService {
	@Autowired
	private CvRepository cvRepository;
	@Override
	public CV save(CV cv) {
		CV savedCv = cvRepository.save(cv);
		
		return savedCv;
	}

}
