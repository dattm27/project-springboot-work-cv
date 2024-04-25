package com.workcv.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workcv.model.Company;
import com.workcv.repository.CompanyRepository;

@Service
public class CompanyService {
	@Autowired
	private CompanyRepository companyRepository ;
	
	
	public Company save(Company company) {
		
		Company savedCompany = companyRepository.save(company);
		return savedCompany;
		
	}


	public Company getCompanyByUserId(int id) {
		Optional<Company> findByUserId = Optional.ofNullable(companyRepository.findCompanyByUserId(id));
		Company company = findByUserId.get();
		return company;
		
		
	}
	
}
