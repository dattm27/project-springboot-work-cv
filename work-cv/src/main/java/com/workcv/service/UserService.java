package com.workcv.service;

import java.util.List;

import com.workcv.model.User;

public interface UserService {
	List<User> getAllUsers();

	boolean authenticate(String email, String password);
	boolean isEmailExisted(String email);
	boolean registerUser(String fullName, String email, String password, String role);
}
