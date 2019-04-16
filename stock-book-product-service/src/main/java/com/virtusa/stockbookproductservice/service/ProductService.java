package com.virtusa.stockbookproductservice.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.virtusa.stockbookproductservice.domain.Category;
import com.virtusa.stockbookproductservice.domain.Product;
import com.virtusa.stockbookproductservice.exception.ProductNameAlreadyExistException;
import com.virtusa.stockbookproductservice.exception.ProductNotFoundException;
import com.virtusa.stockbookproductservice.repository.ICategoryRepository;
import com.virtusa.stockbookproductservice.repository.IProductRepository;

@Service
public class ProductService {

	@Autowired
	IProductRepository productRepository;

	@Autowired
	ICategoryRepository categoryRespositoty;

	// save the product --done 
	@Transactional
	public Product saveProduct(Product product) {
		
		
		Category theCategory = null;
		Product theProduct = null;

		
		Optional<Category> optCategory = categoryRespositoty.findById(product.getCategory().getId());

		if (optCategory.isPresent()) {
			try {
				theCategory = optCategory.get();
				theCategory.add(product);
				theProduct = productRepository.save(product);
			} catch (Exception e) {
				throw new ProductNameAlreadyExistException("Product name already exists!!");
			}
		}

		return theProduct;
		  
	//	return productRepository.save(product);
	}

	// get product by id --done
	@Transactional
	public Product getProductById(Long id) {
		Optional<Product> optProduct = productRepository.findById(id);
		if (optProduct.isPresent()) {
			return  optProduct.get();
		}
		else 
			throw new ProductNotFoundException("Product not found!!");
		
	}

	// get list of products --done
	@Transactional
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	//update Product by id
	@Transactional
	public Product updateProductById(Long id, Product product) {
		
		Product theProduct = null;
		Optional<Product> optProduct = productRepository.findById(id);

		if (optProduct.isPresent()) {
			theProduct = optProduct.get();
			System.out.println(theProduct.getCategory().getName());
			theProduct.setName(product.getName());
			theProduct.setDescription(product.getDescription());
			theProduct.setCategory(product.getCategory());
			System.out.println(theProduct.getCategory().getName());
			return productRepository.save(theProduct);
		}
		else
			throw new ProductNotFoundException("Product not found!!");
	}

	@Transactional
	public Product deleteProductById(Long id) {
		Product theProduct = null;
		Optional<Product> optCategory = productRepository.findById(id);
		if (optCategory.isPresent()) {
			theProduct = optCategory.get();
			productRepository.delete(theProduct);
			return theProduct;
		}
		else
			throw new ProductNotFoundException("Product not found!!");
	}
}
