package com.virtusa.stockbookproductservice.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author shshubham
 *
 */
@Entity
@Table(name = "category")

public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "category",fetch=FetchType.LAZY)
	private List<Product> products;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public List<Product> getProducts() {
		return products;
	}

	
	public void setProducts(List<Product> products) {
		this.products = products;
	}

//constructors
	public Category() {

	}

// field constructor	
	public Category(String name, List<Product> products) {
		this.name = name;
		this.products = products;
	}

	public Category(String name) {
		this.name = name;
	}
	
	public Category(Long id,String name) {
		this.name = name;
		this.id= id;
	}
	
	
	

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name +  "]";
	}

	// support for bi-directional link
	public void add(Product product) {
		if (products != null)
			products = new ArrayList<Product>();

		products.add(product);

		product.setCategory(this);
	}
	
	
}
