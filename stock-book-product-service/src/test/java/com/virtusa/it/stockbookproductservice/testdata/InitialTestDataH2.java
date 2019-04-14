package com.virtusa.it.stockbookproductservice.testdata;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.virtusa.stockbookproductservice.domain.Category;
import com.virtusa.stockbookproductservice.service.CategoryService;
import com.virtusa.stockbookproductservice.service.ProductService;
import com.virtusa.stockbookproductservice.service.StockService;

@Component
@Profile("test")
public class InitialTestDataH2 implements CommandLineRunner {

	@Autowired 
	CategoryService categoryService;
	
	@Autowired 
	ProductService productService;
	
	@Autowired
	StockService stockService;
	
	
	@Override
	 @Transactional
	public void run(String... args) throws Exception {
		
		Category  category  = new Category( "category1");
		
		Category theCategory  = categoryService.saveCategory(category);
		
		System.out.println("================>>>>"+theCategory);
		
		/*
		 * Product product1 = new Product("product1","description1", category);
		 * product1.setId(101L); Stock stock = new Stock();
		 */
		
		
		
		
	}

}
