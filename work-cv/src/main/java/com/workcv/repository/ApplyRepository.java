package com.workcv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.workcv.model.Apply;
import com.workcv.model.ApplyInfo;
@Repository
public interface ApplyRepository  extends JpaRepository<Apply, Integer>{

	Apply findByUserIdAndJobId(int user_id, int job_id);
	
	  @Query("SELECT u.fullName, u.email, j.title, a.cv.id, a.status " +
	           "FROM User u " +
	           "JOIN Apply a ON u.id = a.user.id " +
	           "JOIN Job j ON a.job.id = j.id " +
	           "WHERE j.company.id = :companyId")
	   List<Object[]> findApplicationDetailsByCompanyId(@Param("companyId") int companyId);
}
