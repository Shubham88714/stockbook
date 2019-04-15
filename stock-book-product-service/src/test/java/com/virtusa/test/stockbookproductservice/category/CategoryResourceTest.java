package com.virtusa.test.stockbookproductservice.category;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import com.virtusa.stockbookproductservice.domain.Category;
import com.virtusa.stockbookproductservice.resource.CategoryResource;
import com.virtusa.stockbookproductservice.service.CategoryService;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class CategoryResourceTest {

	@Mock
	CategoryService categoryService;

	@InjectMocks
	CategoryResource categoryResource;

	//save category
	@Test
	public void saveCategory() throws Exception
	{
		Category category = new Category(101L, "furniture");
		Mockito.when(categoryService.saveCategory(category)).thenReturn(category);
		categoryService.saveCategory(category);
	}
	
	//get all category
	@Test
	public void getAllCategory()
	{
		List<Category> categories = new ArrayList<Category>();
		categories.add(new Category(1L,"cloth"));
		categories.add(new Category(2L,"Grocery"));
		Mockito.when(categoryService.getAllCategories()).thenReturn(categories);
		categoryResource.getAllCategories();
	}

	//get category by id
	@Test
	public void getCategoryById()
	{
		Category category = new Category(101L, "furniture");
		Mockito.when(categoryService.getCategoryById(anyLong())).thenReturn(category);
		categoryResource.getCategoryById(101L);
	}
	
	//update category by id
	@Test
	public void updateCategory()
	{
		Category category = new Category(101L, "furniture");
		Mockito.when(categoryService.updateCategoryById(category.getId(), category))
		.thenReturn(category);
		categoryResource.updateCategory(category.getId(), category);
	}
	
	// delete category by id
	@Test
	public void deleteCategoryByid() throws Exception {

		Category category = new Category(101L, "furniture");

		Mockito.when(categoryService.deleteCategoryById(anyLong())).thenReturn(category);
		categoryResource.deleteCategory(101L);
	}
	


}
