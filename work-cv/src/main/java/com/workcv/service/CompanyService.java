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
		Company findByUserId = (companyRepository.findCompanyByUserId(id));
		Company company = findByUserId;
		return company;
		
		
	}


	public Optional<Company> getCompanyById(int id) {
		
		return companyRepository.findById(id);
	}
	
}
