package com.workcv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workcv.model.Category;
import com.workcv.repository.CategoryRepository;
@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	@Override
	public List<Category> getAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		return categories;
	}
	@Override
	public Category save(Category category) {
		Category savedCategory = categoryRepository.save(category);
		return savedCategory;
	}
	@Override
	public List<Object[]> getTrendyCategories() {
		List<Object[]> trendyCategories= categoryRepository.findAllWithJobCount();
		return trendyCategories;
	}

}
