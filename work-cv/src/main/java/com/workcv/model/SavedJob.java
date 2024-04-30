package com.workcv.model;



import jakarta.persistence.*;

@Entity
@Table(name="saved_job")
public class SavedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Job job;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public SavedJob(int id, User user, Job job) {
		super();
		this.id = id;
		this.user = user;
		this.job = job;
	}

   public SavedJob() {
	   
   }
}