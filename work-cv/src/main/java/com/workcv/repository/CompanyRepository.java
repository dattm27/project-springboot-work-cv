package com.workcv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.workcv.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
	Company findCompanyByUserId(int user_id);

	 @Query("SELECT c.id, c.name, c.email, c.phoneNumber, COUNT(j.id) AS numJobs " +
	           "FROM Company c " +
	           "LEFT JOIN Job j ON c.id = j.company.id " +
	           "GROUP BY c.id, c.name " +
	           "HAVING COUNT(j.id) > 0 " +
	           "ORDER BY COUNT(j.id) DESC " +
	           "LIMIT 8")
	List<Object[]> findTopCompaniesByNumJobs();
}
