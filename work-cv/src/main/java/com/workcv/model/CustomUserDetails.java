package com.workcv.model;

import java.util.Collection;
import java.util.Collections;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

public class CustomUserDetails implements UserDetails {

	
	private static final long serialVersionUID = 1L;
	
	private User user;
	
	
	public CustomUserDetails(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	//kiểm tra xem tài khoản đã kích hoạt chưa thì mới cho đăng nhập
	@Override
	public boolean isEnabled() {
		
		return (user.getStatus()==1);
	}
	public int getId() {
		// TODO Auto-generated method stub
		return user.getId();
	}
	public String getRole() {
		// TODO Auto-generated method stub
		return user.getRole();
	}
	public String getFullName() {
		return user.getFullName();
	}
	public User getUser () {
		return user;
	}
	
}
