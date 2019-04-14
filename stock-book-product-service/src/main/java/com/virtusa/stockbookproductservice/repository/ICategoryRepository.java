package com.virtusa.stockbookproductservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.virtusa.stockbookproductservice.domain.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long>{

	
}
