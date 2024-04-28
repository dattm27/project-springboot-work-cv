package com.workcv.model;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "job")
public class Job {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    @Column(name = "created_date")
	    private Date createdDate;

	    @Column(name = "title")
	    private String title;

	    @Column(name = "quantity")
	    private int quantity;

	    @Column(name = "address")
	    private String address;

	    @Column(name = "type")
	    private String type;

	    @Column(name = "view")
	    private int view;

	    @Column(name = "status")
	    private int status;
	    @Column(name = "experience")
	    private String experience;
	    
	    @Column(name = "description", columnDefinition = "TEXT")
	    private String description;

	    @Column(name = "salary")
	    private String salary;

	    @Column(name = "deadline")
	    private Date deadline;
	    @Column(name = "num_applicants")
	    private int numOfApplicants = 0;
	    @ManyToOne
	    @JoinColumn(name = "category_id", referencedColumnName = "id")
	    private Category category;
	    @ManyToOne
	    @JoinColumn(name = "company_id")
	    private Company company;
	    
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public Date getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public int getView() {
			return view;
		}

		public void setView(int view) {
			this.view = view;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getSalary() {
			return salary;
		}

		public void setSalary(String salary) {
			this.salary = salary;
		}

		public Date getDeadline() {
			return deadline;
		}

		public void setDeadline(Date deadline) {
			this.deadline = deadline;
		}

		public Category getCategory() {
			return category;
		}

		public void setCategory(Category category) {
			this.category = category;
		}

		public String getExperience() {
			return experience;
		}

		public void setExperience(String experience) {
			this.experience = experience;
		}

		public Company getCompany() {
			return company;
		}

		public void setCompany(Company company) {
			this.company = company;
		}

		public int getNumOfApplicants() {
			return numOfApplicants;
		}

		public void setNumOfApplicants(int numOfApplicants) {
			this.numOfApplicants = numOfApplicants;
		}
		
		
	    
}
