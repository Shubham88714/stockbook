package com.virtusa.stockbookproductservice.exception;

public class ProductNameAlreadyExistException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ProductNameAlreadyExistException(String message) {
		super(message);
	}

}
