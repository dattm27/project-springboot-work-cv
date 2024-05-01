package com.workcv.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



import com.workcv.model.Company;
import com.workcv.model.FollowingCompany;
import com.workcv.model.Job;
import com.workcv.model.SavedJob;
import com.workcv.model.User;
import com.workcv.repository.FollowingCompanyRepository;
import com.workcv.repository.SavedJobRepository;
import com.workcv.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import net.bytebuddy.utility.RandomString;
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FollowingCompanyRepository followingCompanyRepository;
	@Autowired 
	private  SavedJobRepository savedJobRepository;
	private final JavaMailSender mailSender;

    UserServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
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
	public User registerUser(String fullName, String email, String password, String role) {
		// Kiểm tra xem email đã tồn tại chưa
        if (userRepository.findByEmail(email) != null) {
            return null; // Email đã tồn tại
        }
        else {
        	// Tạo người dùng mới và lưu vào cơ sở dữ liệu
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role); // Vai trò của người dùng: ứng viên hoặc nhà tuyển dụng
            user.setStatus(0); //chưa kích hoạt
       
            String randomCode = RandomString.make(64);
            user.setVerificationCode(randomCode);
          
        return     userRepository.save(user);
            
    
    	
        }
		
	}
	//gửi mã xác thực vào email cho người dùng khi đăng ký tài khoản mới
	@Override
	public void sendVerificationCode(User user, String siteURL) throws UnsupportedEncodingException, MessagingException {
		String subject = "Vui lòng xác nhận đăng ký tài khoản";
		String sendername  ="Work CV Team";
		String mailContent = "<p>Xin chào, " + user.getFullName() + ", </p>";
		mailContent+="<p>Vui lòng click vào link dưới đây để xác nhận đăng ký tài khoản tại Work CV: </p>";
		String verifyURL = siteURL + "/verify?code=" +user.getVerificationCode();
		mailContent+="<h3><a href=\""+ verifyURL +"\">Xác nhận </a></h3>"  ;
		mailContent+="<p>Xin chân thành cảm ơn, Work CV Team </p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom("dattran2003.ttn@gmail.com", sendername);
		helper.setTo(user.getEmail());
		helper.setSubject(subject);
		helper.setText(mailContent, true);
		
		mailSender.send(message);
		
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

	@Override
	public boolean verify(String verificationCode) {
		User user = userRepository.findByVerificationCode(verificationCode);
		//không thấy tài khoản hoặc đã được kích hoạt rồi
		if(user == null || user.getStatus()==1) {
			return false;
		}
		else {
			user.setStatus(1);
			userRepository.save(user);
			return true;
		}
		
	}
	

	

}
