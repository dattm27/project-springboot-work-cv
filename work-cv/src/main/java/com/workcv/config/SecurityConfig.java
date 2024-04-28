package com.workcv.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.workcv.model.Company;
import com.workcv.service.CompanyService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.security.config.Customizer.withDefaults;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private UserDetailsService userDetailsService;
	

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers("/").permitAll();
			auth.requestMatchers("/user/list").hasAnyAuthority("employer");

			auth.requestMatchers("/user/signup").permitAll();
			auth.requestMatchers("/assets/**").permitAll();
	
			auth.requestMatchers("/logout").permitAll();
			
			auth.requestMatchers("/employer/**").hasAnyAuthority("employer");
			auth.requestMatchers("/job/employer/**").hasAnyAuthority("employer");
			auth.requestMatchers("/job/list").permitAll();
			auth.requestMatchers("/job/details/**").permitAll();
		
			auth.requestMatchers("/profileImage/**").permitAll();
			

			auth.requestMatchers("/candidate.**").hasAnyAuthority("candidate");
			auth.anyRequest().authenticated();

		})
//		.formLogin(withDefaults());
//				.oauth2Login(oauth2Login ->
//	            oauth2Login
//	                .loginPage("/login-form.html") // Sử dụng trang đăng nhập tùy chỉnh
//				)
				.formLogin(formLogin -> formLogin.loginPage("/signin") // Sử dụng trang đăng nhập tùy chỉnh
						.loginProcessingUrl("/login").defaultSuccessUrl("/", true).failureUrl("/signin?error")
						.permitAll() // khi đăng nhập sai -> trả về trang đăng nhập kèm báo lỗi

				).logout(logout -> logout.logoutUrl("/signout") // Endpoint cho việc đăng xuất
						.logoutSuccessUrl("/") // URL sau khi đăng xuất thành công
				).headers(headers -> headers
				        .frameOptions(frameOptions -> frameOptions
				                .sameOrigin() // Hoặc .disable() nếu bạn muốn vô hiệu hóa X-Frame-Options
				            )
				        );

		return http.build();
	};

	// xử lý logic đăng nhập
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return provider;
	}

	// dùng để mã hoá mật khẩu khi đăng ký
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	

}
