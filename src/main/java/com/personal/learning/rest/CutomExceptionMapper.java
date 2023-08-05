package com.personal.learning.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CutomExceptionMapper {
	
	@ExceptionHandler
	public ResponseEntity<String> handleBadRequestException(IllegalArgumentException exception){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
	}
	
}
