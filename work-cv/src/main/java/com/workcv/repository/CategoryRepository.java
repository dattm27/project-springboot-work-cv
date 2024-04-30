package com.workcv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.workcv.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	@Query("SELECT c.id, c.name, COUNT(j.id) " +
	           "FROM Category c " +
	           "LEFT JOIN c.jobs j " +
	           "GROUP BY c.id, c.name "+
				"HAVING COUNT(j.id) > 0 " +
			    "ORDER BY COUNT(j.id) DESC " +
			    "LIMIT 8")
	    List<Object[]> findAllWithJobCount();

}
