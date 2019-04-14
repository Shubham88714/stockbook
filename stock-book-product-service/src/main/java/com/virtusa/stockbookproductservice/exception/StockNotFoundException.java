package com.virtusa.stockbookproductservice.exception;

public class StockNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 4L;

	public StockNotFoundException(String message) {
		super(message);
	}

}
