package com.workcv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workcv.model.Company;
import com.workcv.model.Job;

@Repository
public interface JobRepository  extends JpaRepository<Job, Integer>{
	Job findJobById(int id);

	Job save(Job job);

	List<Job> findJobByCompanyId(int company_id);

	List<Job> findByStatusOrderByViewDesc(int status);	
	List<Job> findByStatusOrderByNumOfApplicantsDesc(int status);

	List<Job> findFirst5ByStatusOrderByNumOfApplicantsDesc(int status);
}
