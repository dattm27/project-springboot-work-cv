package com.workcv.model;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity

@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	private String address;
	@Column
	private String description;
	@Column
	private String email;
	@Column
	private String fullName;
	@Column
	private String image;
	@Column
	private String password;
	@Column(name = "phone_number")
	private String phoneNumber;
	@Column
	private int status;
	@Column
	private String role;
	@OneToOne(mappedBy = "user")
	private CV cv;
	@OneToOne(mappedBy = "user")

	private Company company;

//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<FollowingCompany> followingCompanies;

//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<SavedJob> savedJobs;

	public int getId() {

		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phone_number) {
		this.phoneNumber = phone_number;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public CV getCv() {
		return cv;
	}

	public void setCv(CV cv) {
		this.cv = cv;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<FollowingCompany> getFollowingCompanies() {
		return followingCompanies;
	}

	public void setFollowingCompanies(List<FollowingCompany> followingCompanies) {
		this.followingCompanies = followingCompanies;
	}

	public List<SavedJob> getSavedJobs() {
		return savedJobs;
	}

	public void setSavedJobs(List<SavedJob> savedJobs) {
		this.savedJobs = savedJobs;
	}

}
