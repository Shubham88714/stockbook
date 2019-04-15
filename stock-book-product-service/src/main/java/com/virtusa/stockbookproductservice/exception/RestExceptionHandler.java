package com.virtusa.stockbookproductservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {	
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(CategoryDeleteNotValidException e)
	{
		ErrorResponse error = new ErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(e.getMessage());
		error.setTimestamp(System.currentTimeMillis());
		return new ResponseEntity<ErrorResponse>(error,HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> productNotFoundException(ProductNotFoundException e)
	{
		ErrorResponse error = new ErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(e.getMessage());
		error.setTimestamp(System.currentTimeMillis());
		return new ResponseEntity<ErrorResponse>(error,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> stockNotFoundException(StockNotFoundException e)
	{
		ErrorResponse error = new ErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(e.getMessage());
		error.setTimestamp(System.currentTimeMillis());
		return new ResponseEntity<ErrorResponse>(error,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> categoryNotFoundException(CategoryNotFoundException e)
	{
		ErrorResponse error = new ErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(e.getMessage());
		error.setTimestamp(System.currentTimeMillis());
		return new ResponseEntity<ErrorResponse>(error,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> productNameAlreadyExistException(ProductNameAlreadyExistException e)
	{
		ErrorResponse error = new ErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(e.getMessage());
		error.setTimestamp(System.currentTimeMillis());
		return new ResponseEntity<ErrorResponse>(error,HttpStatus.BAD_REQUEST);
		
	}
	
}
