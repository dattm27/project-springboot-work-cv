package com.workcv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workcv.model.Apply;
@Repository
public interface ApplyRepository  extends JpaRepository<Apply, Integer>{

	Apply findByUserIdAndJobId(int user_id, int job_id);
	

}
