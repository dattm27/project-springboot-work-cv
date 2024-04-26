package com.workcv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workcv.model.Company;
import com.workcv.model.Job;

@Repository
public interface JobRepository  extends JpaRepository<Company, Integer>{
	Job findJobById(int id);

	Job save(Job job);
}
