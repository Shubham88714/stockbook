package com.virtusa.it.stockbookproductservice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.virtusa.stockbookproductservice.StockBookProductServiceApplication;
import com.virtusa.stockbookproductservice.domain.Category;
import com.virtusa.stockbookproductservice.domain.Product;
import com.virtusa.stockbookproductservice.exception.ErrorResponse;
import com.virtusa.stockbookproductservice.repository.ICategoryRepository;
import com.virtusa.stockbookproductservice.repository.IProductRepository;
import com.virtusa.stockbookproductservice.service.CategoryService;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { StockBookProductServiceApplication.class })
public class CategoryIntegrationTest {

	@Autowired
	WebApplicationContext wac;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	ICategoryRepository categoryRepository;

	@Autowired
	IProductRepository productRepository;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test // save category test
	public void saveCategoryTest() throws Exception {
		Category category = new Category("category1");

		mockMvc.perform(
				post("/api/category").contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(category)))
				.andExpect(status().isCreated());
	}

	@Test
	public void getCategoryById() throws Exception {
		Category category2 = new Category("category2");

		Category theCategory = categoryRepository.save(category2);

		String getResponse = mockMvc.perform(get("/api/category/" + theCategory.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn().getResponse()
				.getContentAsString();

		Category jsonCategory = mapper.readValue(getResponse, Category.class);

		assertEquals(category2.getName(), jsonCategory.getName());

	}

	@Test // negative test
	public void getCategoryByWrongId() throws Exception {
		String action = mockMvc.perform(get("/api/category/55884")).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn().getResponse()
				.getContentAsString();

		ErrorResponse error = mapper.readValue(action, ErrorResponse.class);

		assertEquals(error.getStatus(), 400);
		assertEquals(error.getMessage(), "category not found!!");
	}

	@Test
	public void getAllCategory() throws UnsupportedEncodingException, Exception {
		Category category3 = new Category("category3");
		Category category4 = new Category("category4");

		Category theCategory3 = categoryRepository.save(category3);
		Category theCategory4 = categoryRepository.save(category4);

		String result = mockMvc.perform(get("/api/categories")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn().getResponse()
				.getContentAsString();

		/*
		 * Category[] categories = mapper.readValue(result, Category[].class);
		 * 
		 * 
		 * assertEquals(categories[0].getName(), category3.getName());
		 * 
		 * assertEquals(categories[1].getName(), category4.getName());
		 */
	}

	@Test
	public void updateCategory() throws Exception {
		Category category5 = new Category("category 5");

		category5 = categoryRepository.save(category5);

		Category updateCategory = new Category(category5.getId(), "updated category5");

		System.out.println(updateCategory);
		String result = mockMvc
				.perform(put("/api/category/" + category5.getId()).contentType(MediaType.APPLICATION_JSON)
						.content(new Gson().toJson(updateCategory)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		Category jsonCategory = mapper.readValue(result, Category.class);

		assertEquals(updateCategory.getId(), jsonCategory.getId());
		assertEquals(updateCategory.getName(), jsonCategory.getName());

	}

	@Test // negative test
	public void updateCategoryByWrongId() throws Exception {

		Category updateCategory = new Category(45L, "updated category5");

		String result = mockMvc
				.perform(put("/api/category/" + updateCategory.getId()).contentType(MediaType.APPLICATION_JSON)
						.content(new Gson().toJson(updateCategory)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

		ErrorResponse error = mapper.readValue(result, ErrorResponse.class);

		assertEquals(error.getStatus(), 400);
		assertEquals(error.getMessage(), "category not found!!");

	}

	@Test
	public void deleteCategoryById() throws Exception {
		Category deleteCategory = new Category("deleteCategory");
		deleteCategory = categoryRepository.save(deleteCategory);

		String deleteResponse = mockMvc.perform(delete("/api/category/" + deleteCategory.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn().getResponse().getContentAsString();

		Category jsonCategory = mapper.readValue(deleteResponse, Category.class);

		assertEquals(deleteCategory.getId(), jsonCategory.getId());
		assertEquals(deleteCategory.getName(), jsonCategory.getName());
	}

	@Test // negative test
	public void deleteCategoryByWrongId() throws Exception {

		Category deleteCategory = new Category(45L, "delete category6");

		String result = mockMvc.perform(delete("/api/category/" + deleteCategory.getId()))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn().getResponse().getContentAsString();

		ErrorResponse error = mapper.readValue(result, ErrorResponse.class);

		assertEquals(error.getStatus(), 400);
		assertEquals(error.getMessage(), "category not found!!");

	}

	
	//--not done--exception is not getting raised
	@Test // negative test
	public void deleteCategoryAssociatedWithProduct() throws  Exception {
		Category pCategory = new Category("pCategory");
		Product product1 = new Product("product1", "description1", pCategory);

		Product theProduct = productRepository.save(product1);
		Category theCategory = theProduct.getCategory();

		System.out.println(theCategory.getId());
		String result = mockMvc.perform(delete("/api/category/" + theCategory.getId()))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn().getResponse().getContentAsString();

		ErrorResponse error = mapper.readValue(result, ErrorResponse.class);

		assertEquals(error.getStatus(), 400);
		assertEquals(error.getMessage(), "you have to first delete the products of this category");

	}
}
