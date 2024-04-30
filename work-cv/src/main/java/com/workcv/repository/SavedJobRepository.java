package com.workcv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workcv.model.Job;
import com.workcv.model.SavedJob;
import com.workcv.model.User;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Integer> {
    List<SavedJob> findByUser(User user);

	SavedJob findByUserAndJob(User user, Job job);
}