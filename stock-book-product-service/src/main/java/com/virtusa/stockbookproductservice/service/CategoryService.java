package com.virtusa.stockbookproductservice.service;

import java.sql.SQLException;
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

	// save --done
	@Transactional
	public Category saveCategory(Category category) {
		return categoryRepository.save(category);
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

	// get list by id --done
	@Transactional
	public Category getCategoryById(Long id) {
		Optional<Category> optCategory = categoryRepository.findById(id);

		if (optCategory.isPresent()) {
			return optCategory.get();
		} else
			throw new CategoryNotFoundException("category not found!!");
	}

	// update --done
	@Transactional
	public Category updateCategoryById(Long id, Category category) {
		Category theCategory = null;
		Optional<Category> optCategory = categoryRepository.findById(id);

		if (optCategory.isPresent()) {
			theCategory = optCategory.get();
			theCategory.setName(category.getName());
			return categoryRepository.save(theCategory);
		} else
			throw new CategoryNotFoundException("category not found!!");
	}

	// delete by id
	@Transactional
	public Category deleteCategoryById(Long id) {
		Category theCategory = null;
		Optional<Category> optCategory = categoryRepository.findById(id);
		if (optCategory.isPresent()) {
			try {
				theCategory = optCategory.get();
				categoryRepository.delete(theCategory);
				System.out.println("i m try block");
			} catch (Exception e) {
				System.out.println("catch block====================");
			}
			
		} else
			throw new CategoryNotFoundException("category not found!!");
		
		return theCategory;
	}

}
