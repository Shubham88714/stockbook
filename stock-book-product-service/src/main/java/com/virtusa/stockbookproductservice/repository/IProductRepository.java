package com.virtusa.stockbookproductservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.virtusa.stockbookproductservice.domain.Product;

public interface IProductRepository  extends JpaRepository<Product, Long>{

}
