package com.workcv.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cv")
public class CV {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "filename")
	private String filename;

	@OneToOne
	@JoinColumn(name = "user_id",  referencedColumnName = "id")
	private User user;



	// Getters and setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
