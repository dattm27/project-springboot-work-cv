package com.workcv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workcv.model.User;
import com.workcv.repository.UserRepository;
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
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
	public boolean isEmailExisted(String email) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
