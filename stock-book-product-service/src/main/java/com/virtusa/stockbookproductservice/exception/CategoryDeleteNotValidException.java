package com.virtusa.stockbookproductservice.exception;

public class CategoryDeleteNotValidException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CategoryDeleteNotValidException(String message)
	{
		super(message);
	}
}
