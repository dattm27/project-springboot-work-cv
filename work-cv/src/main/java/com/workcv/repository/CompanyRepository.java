package com.workcv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workcv.model.Company;
@Repository
public interface CompanyRepository  extends JpaRepository<Company, Integer> {
	Company findCompanyByUserId(int user_id);
}
