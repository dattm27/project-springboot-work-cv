package com.workcv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workcv.model.CV;

@Repository
public interface CvRepository extends JpaRepository<CV, Integer>{
	
}
