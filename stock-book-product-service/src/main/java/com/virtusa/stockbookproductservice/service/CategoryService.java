package com.virtusa.stockbookproductservice.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtusa.stockbookproductservice.domain.Category;
import com.virtusa.stockbookproductservice.exception.CategoryDeleteNotValidException;
import com.virtusa.stockbookproductservice.exception.CategoryNotFoundException;
import com.virtusa.stockbookproductservice.repository.ICategoryRepository;

@Service
public class CategoryService {

	
	
	@Autowired
	ICategoryRepository categoryRepository;

	// save
	public Category saveCategory(Category category) {
		return categoryRepository.save(category);
	}

	// delete by id------------------try and catch 
	public Category deleteCategory(Long id) {
		Category theCategory = null;
		Optional<Category> optCategory = categoryRepository.findById(id);
		if (optCategory.isPresent()) {
			theCategory = optCategory.get();
		
			try {
				categoryRepository.delete(theCategory);
			} catch (Exception e) {
				
					throw new CategoryDeleteNotValidException("you have to first delete the products of this category");
			}
		}
	
		return theCategory;
	}

	/*
	 * // update by id public Category updateCategory(Category category) { Category
	 * theCategory = null; Optional<Category> optCategory =
	 * categoryRepository.findById(category.getId());
	 * 
	 * if (optCategory.isPresent()) { theCategory = optCategory.get();
	 * theCategory.setName(category.getName());
	 * 
	 * return categoryRepository.save(theCategory); } return theCategory; }
	 */

	// get list of saved category
	@Transactional
	public List<Category> getAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		categories.forEach((n) -> n.getProducts());
		categories.forEach((n) -> System.out.println(n));
		return categories;
	}

	// get list by id
	public Category getCategoryById(Long id) {
		Optional<Category> optCategory = categoryRepository.findById(id);
		
		if (optCategory.isPresent()) {
			return optCategory.get();
		}
		else
			throw new CategoryNotFoundException("category not found!!");
	}

	//update
	public Category updateCategoryById(Long id,Category category) {
		Category theCategory = null;
		Optional<Category> optCategory = categoryRepository.findById(id);

		if (optCategory.isPresent()) {
			theCategory = optCategory.get();
			theCategory.setName(category.getName());
			return categoryRepository.save(theCategory);
		}
		else 
			throw new CategoryNotFoundException("category not found!!");
	}
}
