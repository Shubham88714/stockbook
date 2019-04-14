package com.virtusa.stockbookproductservice.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtusa.stockbookproductservice.domain.Product;
import com.virtusa.stockbookproductservice.domain.Stock;
import com.virtusa.stockbookproductservice.exception.ProductNotFoundException;
import com.virtusa.stockbookproductservice.exception.StockNotFoundException;
import com.virtusa.stockbookproductservice.repository.IProductRepository;
import com.virtusa.stockbookproductservice.repository.IStockRepository;

@Service
public class StockService {

	@Autowired
	private IStockRepository stockRepository;

	@Autowired
	private IProductRepository productRepository;

	// save stock
	@Transactional
	public Stock saveStock(Stock stock) {
		Product theProduct = null;
		Stock theStock = null;

		Optional<Product> optProduct = productRepository.findById(stock.getProductId());

		if (optProduct.isPresent()) {
			theProduct = optProduct.get();
			theProduct.addStock(stock);
			theStock = stockRepository.save(stock);
		}

		return theStock;
	}

	// get list of stock by product id
	@Transactional
	public List<Stock> getStockListByProductId(Long id) {

		List<Stock> stockList = null;
		stockList = stockRepository.getStockListByProductId(id);
		if (stockList.size() != 0)
			return stockList;
		else
			throw new ProductNotFoundException("Product is not available..Check the product id!");
	}

	// get stock by id
	@Transactional
	public Stock getStockById(Long id) {

		Optional<Stock> optStock = stockRepository.findById(id);

		if (optStock.isPresent()) 
			return optStock.get();
		
		else 
			throw new StockNotFoundException("Stock Id not available");

	}

	//update stock by stock id
	@Transactional
	public Stock updateStockByStockId(Stock stock) {
		
		Stock theStock = null;
		
		Optional<Stock> optStock = stockRepository.findById(stock.getId());
		
		if(optStock.isPresent())
		{
			theStock = optStock.get();
			
			theStock.setDate(stock.getDate());
			theStock.setQuantity(stock.getQuantity());
			theStock.setManufacturer(stock.getManufacturer());
			theStock.setCostPrice(stock.getCostPrice());
			theStock.setSellingPrice(stock.getSellingPrice());
			theStock.setDiscount(stock.getDiscount());
			theStock.setTotalCp(stock.getTotalCp());
			theStock.setGst(stock.getGst());
			theStock.setThreshold(stock.getThreshold());
			theStock.setProductId(stock.getProductId());
			
			return stockRepository.save(theStock);
			
		}
		else {
			throw new StockNotFoundException("Stock by id: "+stock.getId()+" is not present");
		}
		
	}

}
