package com.workcv.service;

import java.util.List;

import com.workcv.model.Company;
import com.workcv.model.Job;
import com.workcv.model.SavedJob;
import com.workcv.model.User;

public interface UserService {
	List<User> getAllUsers();

	boolean authenticate(String email, String password);
	String getFullnameByEmail(String email);
	boolean registerUser(String fullName, String email, String password, String role);
	String getRoleByEmail(String email);
	User getUserByEmail(String email);

	User save(User user);
	
	void followCompany(User user, Company company);
    SavedJob saveJob(User user, Job job);
    void unsaveJob(User user, Job job) ;
    List<Company> getFollowedCompanies(User user);
    List<Job> getSavedJobs(User user);
}
