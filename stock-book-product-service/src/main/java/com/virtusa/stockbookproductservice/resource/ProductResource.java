package com.virtusa.stockbookproductservice.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.virtusa.stockbookproductservice.domain.Category;
import com.virtusa.stockbookproductservice.domain.Product;
import com.virtusa.stockbookproductservice.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductResource {

	@Autowired
	ProductService productService;

	// save product --done
	@PostMapping("/product")
	public ResponseEntity<Product> saveProduct(@RequestBody Product product) throws URISyntaxException {
		Product theProduct = productService.saveProduct(product);
		return ResponseEntity.created(new URI("/api/product/" + theProduct.getId())).body(theProduct);
	}

	// get product by id --done
	@GetMapping("/product/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {

		Product theProduct = productService.getProductById(id);
		return new ResponseEntity<Product>(theProduct, HttpStatus.OK);
	}

	@GetMapping("/products")
	@ResponseBody
	public ResponseEntity<List<Product>> getAllProduct() {
		List<Product> products = productService.getAllProducts();
		return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
	}

	// update product also can update category --done
	@PutMapping("/product/{id}")
	public ResponseEntity<Product> updateCategory(@PathVariable("id") Long id, @RequestBody Product product) {
		Product theProduct = productService.updateProductById(id, product);
		return new ResponseEntity<Product>(theProduct, HttpStatus.OK);
	}

	//delete product --done
	@DeleteMapping("/product/{id}")
	public ResponseEntity<Product> deleteProductById(@PathVariable("id") Long id) {
		Product theProduct = productService.deleteProductById(id);
			return new ResponseEntity<Product>(theProduct, HttpStatus.OK);
	}
}
