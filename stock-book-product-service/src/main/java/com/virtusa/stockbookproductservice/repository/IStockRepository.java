package com.virtusa.stockbookproductservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.virtusa.stockbookproductservice.domain.Stock;

@Repository
public interface IStockRepository extends JpaRepository<Stock, Long>{

	@Query("SELECT s FROM Stock s WHERE s.productId = :id")
	public List<Stock> getStockListByProductId(@Param("id") Long id);
	
}
