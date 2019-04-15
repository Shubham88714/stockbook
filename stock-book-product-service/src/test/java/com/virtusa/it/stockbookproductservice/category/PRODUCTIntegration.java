package com.virtusa.it.stockbookproductservice.category;

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
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.virtusa.stockbookproductservice.StockBookProductServiceApplication;
import com.virtusa.stockbookproductservice.domain.Category;
import com.virtusa.stockbookproductservice.domain.Product;
import com.virtusa.stockbookproductservice.domain.Stock;
import com.virtusa.stockbookproductservice.exception.ErrorResponse;
import com.virtusa.stockbookproductservice.repository.ICategoryRepository;
import com.virtusa.stockbookproductservice.repository.IProductRepository;
import com.virtusa.stockbookproductservice.repository.IStockRepository;
import com.virtusa.stockbookproductservice.service.CategoryService;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { StockBookProductServiceApplication.class })
public class PRODUCTIntegration {

	@Autowired
	WebApplicationContext wac;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	ICategoryRepository categoryRepository;

	@Autowired
	IProductRepository productRepository;

	@Autowired
	IStockRepository stockRepository;
	
	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

	}

	@Test // done --- save by passing whole category object with id itself
	public void saveProduct() throws Exception {

		Category category = new Category("category1");
		Category theCategory = categoryRepository.save(category);
		Product product1 = new Product();
		product1.setName("name");
		product1.setDescription("description");
		product1.setCategory(new Category());

		product1.getCategory().setId(theCategory.getId());

		String saveResponse = mockMvc
				.perform(post("/api/product").contentType(MediaType.APPLICATION_JSON)
						.content(new Gson().toJson(product1)))
				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn().getResponse().getContentAsString();

		Product jsonProduct = mapper.readValue(saveResponse, Product.class);

		assertEquals(product1.getName(), jsonProduct.getName()); //product name
		assertEquals(product1.getDescription(),jsonProduct.getDescription()); // product description
		assertEquals(product1.getCategory().getId(), jsonProduct.getCategory().getId()); //category id
		assertEquals(theCategory.getName(), jsonProduct.getCategory().getName()); //category name
	}

	
	@Test //negative test for saving existing name of product
	public void saveAlreadyPresentProductName() throws UnsupportedEncodingException, Exception {
		Category category2 = new Category("category2");
		Category theCategory = categoryRepository.save(category2);
		Product product2 = new Product("name2","description2", theCategory);
		productRepository.save(product2);
		
		Product product3 = new Product("name2","description3", new Category());

		product3.getCategory().setId(theCategory.getId());

		String saveResponse = mockMvc
				.perform(post("/api/product").contentType(MediaType.APPLICATION_JSON)
						.content(new Gson().toJson(product3)))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn().getResponse().getContentAsString();
		
		ErrorResponse errorResponse = mapper.readValue(saveResponse,ErrorResponse.class);
		
		assertEquals(400, errorResponse.getStatus());
		assertEquals("Product name already exists!!", errorResponse.getMessage());
		
	}
	 
	
	@Test
	public void getProductById() throws Exception
	{
		Category category3 = new Category("category3");
		category3 = categoryRepository.save(category3);
		Product product3 = new Product("name3","description3", category3);
		Product theProduct = productRepository.save(product3);
		
		Stock stock1 = new Stock();
		stock1.setDate("2017/09/10");
		stock1.setQuantity(200L);
		stock1.setManufacturer("manufacturer1");
		stock1.setCostPrice(10D);
		stock1.setSellingPrice(20D);
		stock1.setDiscount(2f);
		stock1.setTotalCp(200 * 10D);
		stock1.setGst(100f);
		stock1.setThreshold(50L);
		stock1.setProductId(theProduct.getId());
		
	stockRepository.save(stock1);
		
		

		Stock stock2 = new Stock();
		stock2.setDate("2018/09/10");
		stock2.setQuantity(200L);
		stock2.setManufacturer("manufacturer2");
		stock2.setCostPrice(10D);
		stock2.setSellingPrice(20D);
		stock2.setDiscount(2f);
		stock2.setTotalCp(200 * 10D);
		stock2.setGst(100f);
		stock2.setThreshold(50L);
		stock2.setProductId(theProduct.getId());
		
		stockRepository.save(stock2);

		String getResponse = mockMvc.perform(get("/api/product/" + theProduct.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn().getResponse()
				.getContentAsString();
		
		Product jsonProduct = mapper.readValue(getResponse,Product.class);
		
		assertEquals(product3.getName(), jsonProduct.getName());
		assertEquals(product3.getDescription(), jsonProduct.getDescription());
		assertEquals(category3.getId(), jsonProduct.getCategory().getId());
		assertEquals(category3.getName(), jsonProduct.getCategory().getName());
		
		List<Stock> jsonStockList = jsonProduct.getStockList();
		
		assertEquals(jsonStockList.get(0).getDate(), stock1.getDate());
		assertEquals(jsonStockList.get(0).getProductId(), stock1.getProductId());
		assertEquals(jsonStockList.get(0).getManufacturer(), stock1.getManufacturer());
		
		assertEquals(jsonStockList.get(1).getDate(), stock2.getDate());
		assertEquals(jsonStockList.get(1).getProductId(), stock2.getProductId());
		assertEquals(jsonStockList.get(1).getManufacturer(), stock2.getManufacturer());
	}
	
	@Test //negative test for get by id
	public void getProductByWrongId() throws UnsupportedEncodingException, Exception
	{
		String getResponse = 
				mockMvc.perform(get("/api/product/45"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn().getResponse().getContentAsString();
		
		ErrorResponse errorResponse = mapper.readValue(getResponse, ErrorResponse.class);
		assertEquals(400, errorResponse.getStatus());
		assertEquals("", errorResponse.getMessage());
		
	}

}
