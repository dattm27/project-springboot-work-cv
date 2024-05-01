package com.workcv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workcv.model.Company;
import com.workcv.model.FollowingCompany;
import com.workcv.model.User;

@Repository
public interface FollowingCompanyRepository extends JpaRepository<FollowingCompany, Integer> {
    List<FollowingCompany> findByUser(User user);
    FollowingCompany findByUserAndCompany(User user, Company company);
}
