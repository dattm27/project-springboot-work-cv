package com.workcv.service;
import java.util.List;

import com.workcv.model.Category;
public interface CategoryService {
	List<Category> getAllCategories ();
	Category save(Category category);
}
