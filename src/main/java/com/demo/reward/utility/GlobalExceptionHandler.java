package com.demo.reward.utility;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.demo.reward.exception.InvalidDateException;
import com.demo.reward.exception.TransactionNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo> exceptionHandler(Exception exception) {
		log.error("Transaction not found: {}", exception.getMessage());
		ErrorInfo errorInfo = new ErrorInfo(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(TransactionNotFoundException.class)
	public ResponseEntity<ErrorInfo> transactionsExceptionHandler(TransactionNotFoundException exception) {
		log.error("Transaction not found: {}", exception.getMessage());
		ErrorInfo errorInfo = new ErrorInfo(exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(errorInfo, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidDateException.class)
	public ResponseEntity<ErrorInfo> invalidDateExceptionHandler(InvalidDateException exception){
		log.error("Invalid date input: {}", exception.getMessage());
		ErrorInfo error = new ErrorInfo( exception.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
		return ResponseEntity.badRequest().body(error);
	}

}
