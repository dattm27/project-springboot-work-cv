package com.workcv.model;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "apply")
public class Apply {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "created_at")
    private Date createdAt;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToOne
    @JoinColumn(name = "job_id")
    private Job job;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
    
    @Column(name = "cv")
    private String cv;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCv() {
		return cv;
	}

	public void setCv(String cv) {
		this.cv = cv;
	}

    
}
