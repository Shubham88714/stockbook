package com.virtusa.it.stockbookproductservice;

import static org.junit.Assert.assertEquals;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.virtusa.stockbookproductservice.StockBookProductServiceApplication;
import com.virtusa.stockbookproductservice.domain.Category;
import com.virtusa.stockbookproductservice.domain.Product;
import com.virtusa.stockbookproductservice.domain.Stock;
import com.virtusa.stockbookproductservice.exception.ErrorResponse;
import com.virtusa.stockbookproductservice.repository.IProductRepository;
import com.virtusa.stockbookproductservice.repository.IStockRepository;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { StockBookProductServiceApplication.class })
public class StockIntegrationTest  {

	@Autowired
	WebApplicationContext wac;

	@Autowired
	ObjectMapper mapper;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Autowired
	IProductRepository productRepository;

	@Autowired
	IStockRepository stockRepository;

	@Test //done
	public void saveStockTest() throws Exception {
		Category category = new Category("product category1");
		Product product = new Product("product name1", "product description1", category);

		Product theProduct = productRepository.save(product);
		System.out.println(theProduct);

		Long productId = theProduct.getId();

		Stock stock = new Stock();
		stock.setDate("1196/09/24");
		stock.setQuantity(200L);
		stock.setManufacturer("manufacturer1");
		stock.setCostPrice(10D);
		stock.setSellingPrice(20D);
		stock.setDiscount(2f);
		stock.setTotalCp(200 * 10D);
		stock.setGst(100f);
		stock.setThreshold(50L);
		stock.setProductId(productId);
		System.out.println(stock);

		String postResult = mockMvc
				.perform(MockMvcRequestBuilders.post("/api/stock").contentType(MediaType.APPLICATION_JSON)
						.content(new Gson().toJson(stock)))
				.andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getHeader("Location");

		System.out.println(postResult);
	}

	@Test //
	public void getStockListByProductId() throws Exception {
		Category category = new Category("product category2");
		Product product = new Product("product name2", "product description2", category);

		Product theProduct = productRepository.save(product);
		System.out.println(theProduct);

		Long productId = theProduct.getId();

		Stock stock = new Stock();
		stock.setDate("2010/09/10");
		stock.setQuantity(200L);
		stock.setManufacturer("manufacturer2");
		stock.setCostPrice(10D);
		stock.setSellingPrice(20D);
		stock.setDiscount(2f);
		stock.setTotalCp(200 * 10D);
		stock.setGst(100f);
		stock.setThreshold(50L);
		stock.setProductId(productId);

		Stock theStock = stockRepository.save(stock);

		System.out.println(theStock);

		ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get("/api/stock/product/" + productId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));

		String response = action.andReturn().getResponse().getContentAsString();
		Stock[] stockList = mapper.readValue(response, Stock[].class);

		assertEquals(stockList[0].getId(), theStock.getId());
		assertEquals(stockList[0].getDate(), theStock.getDate());
		assertEquals(stockList[0].getQuantity(), theStock.getQuantity());
		assertEquals(stockList[0].getManufacturer(), theStock.getManufacturer());
		assertEquals(stockList[0].getCostPrice(), theStock.getCostPrice());
		assertEquals(stockList[0].getSellingPrice(), theStock.getSellingPrice());
		assertEquals(stockList[0].getDiscount(), theStock.getDiscount());
		assertEquals(stockList[0].getTotalCp(), theStock.getTotalCp());
		assertEquals(stockList[0].getGst(), theStock.getGst());
		assertEquals(stockList[0].getThreshold(), theStock.getThreshold());
		assertEquals(stockList[0].getProductId(), theStock.getProductId());

	}

	@Test //negative test
	public void getStockListByWrongProductId() throws Exception {
		ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get("/api/stock/product/55884"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));

		String response = action.andReturn().getResponse().getContentAsString();

		ErrorResponse error = mapper.readValue(response, ErrorResponse.class);

		assertEquals(error.getStatus(), 400);
		assertEquals(error.getMessage(), "Product is not available..Check the product id!");

	}

	
	@Test //positive test
	public void getStockById() throws Exception
	{
		Category category = new Category("product category3");
		Product product = new Product("product name3", "product description", category);

		Product theProduct = productRepository.save(product);
		Long productId = theProduct.getId();
		
		Stock stock = new Stock();
		stock.setDate("2018/20/02");
		stock.setQuantity(200L);
		stock.setManufacturer("manufacturer2");
		stock.setCostPrice(10D);
		stock.setSellingPrice(20D);
		stock.setDiscount(2f);
		stock.setTotalCp(200 * 10D);
		stock.setGst(100f);
		stock.setThreshold(50L);
		stock.setProductId(productId);
	
		Stock theStock = stockRepository.save(stock);
		
		System.out.println(stock);
		
		
		ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get("/api/stock/"+theStock.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));

		String response = action.andReturn().getResponse().getContentAsString();

		Stock JsonStock = mapper.readValue(response, Stock.class);
		
		assertEquals(JsonStock.getId(), theStock.getId());
		assertEquals(JsonStock.getDate(), stock.getDate());
		assertEquals(JsonStock.getQuantity(), stock.getQuantity());
		assertEquals(JsonStock.getProductId(), stock.getProductId());
	}
	
	@Test //negative test
	public void getStockByWrongId() throws Exception {
		ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get("/api/stock/45"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));

		String response = action.andReturn().getResponse().getContentAsString();

		ErrorResponse error = mapper.readValue(response, ErrorResponse.class);

		assertEquals(error.getStatus(), 400);
		assertEquals(error.getMessage(), "Stock Id not available");
		
	}

	
	@Test
	public void updateStockById() throws Exception
	{
		//save stock first
		Category category = new Category("product category4");
		Product product = new Product("product name4", "product description4", category);

		Product theProduct = productRepository.save(product);
		System.out.println(theProduct);

		Long productId = theProduct.getId();

		Stock stock = new Stock();
		stock.setDate("2014/09/24");
		stock.setQuantity(200L);
		stock.setManufacturer("manufacturer4");
		stock.setCostPrice(10D);
		stock.setSellingPrice(20D);
		stock.setDiscount(2f);
		stock.setTotalCp(200 * 10D);
		stock.setGst(100f);
		stock.setThreshold(50L);
		stock.setProductId(productId);
		System.out.println(stock);

		Stock theStock = stockRepository.save(stock);
		
		//update stock
		Stock updateStock = new Stock();
		updateStock.setId(theStock.getId());
		updateStock.setDate("2012/09/24");
		updateStock.setQuantity(200L);
		updateStock.setManufacturer("manufacturer4");
		updateStock.setCostPrice(10D);
		updateStock.setSellingPrice(20D);
		updateStock.setDiscount(2f);
		updateStock.setTotalCp(200 * 10D);
		updateStock.setGst(100f);
		updateStock.setThreshold(50L);
		
		
		
		ResultActions action = mockMvc
				.perform(MockMvcRequestBuilders.put("/api/stock/"+theStock.getId()).contentType(MediaType.APPLICATION_JSON)
						.content(new Gson().toJson(updateStock)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));

		String response = action.andReturn().getResponse().getContentAsString();
		
		Stock jsonStock = mapper.readValue(response,Stock.class);
		

		assertEquals(jsonStock.getId(),updateStock.getId());
		assertEquals(jsonStock.getDate(),updateStock.getDate());
		assertEquals(jsonStock.getProductId(), updateStock.getProductId());
		
	}

	@Test //negative test
	public void updateStockByWrongId() throws Exception {
		
		Stock stock = new Stock();
		stock.setId(45L);
		stock.setDate("1196/09/24");
		stock.setQuantity(200L);
		stock.setManufacturer("manufacturer1");
		stock.setCostPrice(10D);
		stock.setSellingPrice(20D);
		stock.setDiscount(2f);
		stock.setTotalCp(200 * 10D);
		stock.setGst(100f);
		stock.setThreshold(50L);
		stock.setProductId(1L);
		System.out.println(stock);

		ResultActions action = mockMvc
				.perform(MockMvcRequestBuilders.put("/api/stock/"+stock.getId()).contentType(MediaType.APPLICATION_JSON)
						.content(new Gson().toJson(stock)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));

		String response = action.andReturn().getResponse().getContentAsString();

		ErrorResponse error = mapper.readValue(response, ErrorResponse.class);

		assertEquals(error.getStatus(), 400);
		assertEquals(error.getMessage(), "Stock by id: "+stock.getId()+" is not present");
		
	}
	
	
	
}
