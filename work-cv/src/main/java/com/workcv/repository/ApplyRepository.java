package com.workcv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.workcv.model.Apply;

@Repository
public interface ApplyRepository  extends JpaRepository<Apply, Integer>{

	Apply findByUserIdAndJobId(int user_id, int job_id);
	
	  @Query("SELECT u.fullName, u.email, j.title, a.cv.id, a.status, a.id " +
	           "FROM User u " +
	           "JOIN Apply a ON u.id = a.user.id " +
	           "JOIN Job j ON a.job.id = j.id " +
	           "WHERE j.company.id = :companyId")
	   List<Object[]> findApplicationDetailsByCompanyId(@Param("companyId") int companyId);
	   
	   //lấy ra danh sách job mà một ứng viên đã ứng tuyển
	   @Query("SELECT j.title, j.type, a.createdAt, c.name, a.status,j.id " +
	           "FROM Apply a " +
	           "JOIN a.job j " +
	           "JOIN j.company c " +
	           "WHERE a.user.id = :userId")
	    List<Object[]> findApplicationsByUserId(@Param("userId") int userId);
}
