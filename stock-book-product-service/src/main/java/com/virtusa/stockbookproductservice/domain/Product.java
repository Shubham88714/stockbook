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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

	public Product(String name, String description, Category category) {

		this.name = name;
		this.description = description;
		this.category = category;
	}
	
	public Product(Long id,String name, String description, Category category) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.category = category;
	}

	public Product() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name",unique = true)
	private String name;

	@Column(name = "description")
	private String description;

	@ManyToOne(fetch=FetchType.EAGER,
			cascade= {CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.DETACH})
	@JoinColumn(name = "category_id")
	private Category category;
	
	
	@OneToMany(fetch= FetchType.EAGER,
			cascade= {CascadeType.MERGE,CascadeType.REMOVE,CascadeType.REFRESH})
	@JoinColumn(name="product_id")
	private List<Stock> stockList;
	
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Stock> getStockList() {
		return stockList;
	}

	public void setStockList(List<Stock> stockList) {
		this.stockList = stockList;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description=" + description + ", category=" + category + "]";
	}

	
	//to add the stock for a product
		public void addStock(Stock stock)
		{
			if(stockList==null)
				stockList = new ArrayList<Stock>();
			
			stockList.add(stock);
		}

}
