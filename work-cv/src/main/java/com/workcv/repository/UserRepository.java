package com.workcv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workcv.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	 User findByEmailAndPassword(String email, String password);
	 User findByEmail(String email);
	 User findByVerificationCode(String verifycationCode);
}
