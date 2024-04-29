package com.workcv.service;

import java.util.Optional;

import com.workcv.model.CV;

public interface CvService {
	CV save(CV cv);
	Optional<CV> getCVById(int id);
}
