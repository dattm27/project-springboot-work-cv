package com.workcv.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workcv.model.Company;
import com.workcv.model.FollowingCompany;
import com.workcv.model.Job;
import com.workcv.model.SavedJob;
import com.workcv.model.User;
import com.workcv.repository.FollowingCompanyRepository;
import com.workcv.repository.SavedJobRepository;
import com.workcv.repository.UserRepository;
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FollowingCompanyRepository followingCompanyRepository;
	@Autowired 
	private  SavedJobRepository savedJobRepository;
	@Override
	public List<User> getAllUsers() {
		
		return userRepository.findAll();
	}
	@Override
	public boolean authenticate(String email, String password) {
		//kiểm tra xem có người dùng nào có trùng email và password không 
		User user = userRepository.findByEmailAndPassword(email, password);
	     return user != null;
	}

	@Override
	public boolean registerUser(String fullName, String email, String password, String role) {
		// Kiểm tra xem email đã tồn tại chưa
        if (userRepository.findByEmail(email) != null) {
            return false; // Email đã tồn tại
        }
        else {
        	// Tạo người dùng mới và lưu vào cơ sở dữ liệu
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role); // Vai trò của người dùng: ứng viên hoặc nhà tuyển dụng
            user.setStatus(1);
            userRepository.save(user);
    		return true;
        }
		
	}
	@Override
	public String getFullnameByEmail(String email) {
		User user = userRepository.findByEmail(email);
	    if(user != null) return user.getFullName();
		return null;
	}
	@Override
	public String getRoleByEmail(String email) {
		User user = userRepository.findByEmail(email);
		if (user!= null) return user.getRole();
		return null;
	}
	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	
	}
	@Override
	public User save(User user) {
		User savedUser = userRepository.save(user);
		return savedUser;
	}
	@Override
	public FollowingCompany followCompany(User user, Company company) {
		FollowingCompany followingCompany = new FollowingCompany();
        followingCompany.setUser(user);
        followingCompany.setCompany(company);
        followingCompanyRepository.save(followingCompany);
        return followingCompany;
		
	}
	@Override
	public SavedJob saveJob(User user, Job job) {
		SavedJob savedJob = new SavedJob();
        savedJob.setUser(user);
        savedJob.setJob(job);
        savedJobRepository.save(savedJob);
        return savedJob;
		
	}
	@Override
	public List<Company> getFollowedCompanies(User user) {
		 List<FollowingCompany> followingCompanies = followingCompanyRepository.findByUser(user);
	        return followingCompanies.stream().map(FollowingCompany::getCompany).collect(Collectors.toList());
	}
	@Override
	public List<Job> getSavedJobs(User user) {
		List<SavedJob> savedJobs = savedJobRepository.findByUser(user);
        return savedJobs.stream().map(SavedJob::getJob).collect(Collectors.toList());
   
	}
	@Override
	public void unsaveJob(User user, Job job) {
		 SavedJob savedJob = savedJobRepository.findByUserAndJob(user, job);
		    if (savedJob != null) {
		        savedJobRepository.delete(savedJob);
		    }
		
	}
	@Override
	public void unfollowCompany(User user, Company company) {
		FollowingCompany followingCompany = followingCompanyRepository.findByUserAndCompany(user, company);
		if ( followingCompany != null) {
			followingCompanyRepository.delete(followingCompany);
		}
		
	}
	
	//lấy ra một following company để phục vụ huỷ theo dõi
	@Override
	public FollowingCompany getFollowingCompanyByUserIdAndCompanyId(User user, Company company) {
		FollowingCompany followingCompany = followingCompanyRepository.findByUserAndCompany(user, company);
		return followingCompany;
	}
	

	

}
