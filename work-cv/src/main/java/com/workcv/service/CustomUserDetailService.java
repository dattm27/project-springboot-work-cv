package com.workcv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.workcv.model.User;
import com.workcv.model.CustomUserDetails;
import com.workcv.repository.UserRepository;

//class này chỉ cho Authentication Provider hiểu cần phải làm gì
//trong việc xác thực người dùng bằng các tài khoản lưu trong cơ sở dữ liệu MySQL
@Service
public class CustomUserDetailService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if (user== null) {
			throw new UsernameNotFoundException("User Not Found ");
		}
		return new  CustomUserDetails(user);
	}

}
