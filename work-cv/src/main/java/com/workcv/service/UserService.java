package com.workcv.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.workcv.model.Company;
import com.workcv.model.FollowingCompany;
import com.workcv.model.Job;
import com.workcv.model.SavedJob;
import com.workcv.model.User;
import com.workcv.repository.UserRepository;

import jakarta.mail.MessagingException;

public interface UserService {
	List<User> getAllUsers();

	boolean authenticate(String email, String password);
	String getFullnameByEmail(String email);
	User registerUser(String fullName, String email, String password, String role);
	//gửi email xác thực tài khoản người dùng
	public void sendVerificationCode(User user, String siteURL)  throws UnsupportedEncodingException, MessagingException ;
	
	//tim kiem nguoi dung theo ma xac thuc -> xac thuc nguoi dung
	public boolean verify(String verificationCode) ;
	String getRoleByEmail(String email);
	User getUserByEmail(String email);

	User save(User user);
	
	//theo dõi công ty 
	FollowingCompany followCompany(User user, Company company);
	void unfollowCompany(User user, Company company);
	List<Company> getFollowedCompanies(User user);
	//lẩy ra FollowinCompany có user user và company 
	FollowingCompany getFollowingCompanyByUserIdAndCompanyId(User user , Company company);
	
	//Lưu job
	SavedJob saveJob(User user, Job job);
    void unsaveJob(User user, Job job) ;
    
  
    List<Job> getSavedJobs(User user);
}
