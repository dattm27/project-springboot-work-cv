package com.workcv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import com.workcv.service.UserService;

import static  org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;;
@Configuration
@EnableWebSecurity
public class SecurityConfig  {
	@Autowired
	private UserDetailsService userDetailsService;
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(auth -> {
					auth.requestMatchers("/").permitAll();

					auth.requestMatchers("/employer").hasAnyAuthority("employer");
					auth.requestMatchers("/candidate").hasAnyAuthority("candidate");
					auth.anyRequest().authenticated();
				})
				.oauth2Login(withDefaults())
				.formLogin(withDefaults())
				.build();
	};
	@Bean
 	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider() ;
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return provider;
	}
 	
 	
 	
}
