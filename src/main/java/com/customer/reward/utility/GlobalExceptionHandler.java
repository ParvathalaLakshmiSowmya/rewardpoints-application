package com.customer.reward.utility;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.customer.reward.exception.InvalidDateException;
import com.customer.reward.exception.TransactionNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    Environment environment;
    
    public GlobalExceptionHandler(Environment environment) {
		this.environment = environment;
	}
    
	@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleGlobalException(
            Exception ex, 
            HttpServletRequest request) {

        log.error("Unexpected error occurred", ex);

        ErrorInfo error = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                environment.getProperty("General.EXCEPTION_MESSAGE"),
                request.getRequestURI(),
                environment.getProperty("General.EXCEPTION_DETAILS")
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
	
    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleTransactionNotFound(
            TransactionNotFoundException ex,
            HttpServletRequest request) {

        log.warn("Transaction error: {}", ex.getMessage());

        ErrorInfo error = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                environment.getProperty(ex.getMessage()),
                request.getRequestURI(),
                environment.getProperty("General.EXCEPTION_NOT_FOUND")
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }
    
    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ErrorInfo> handleInvalidDate(
            InvalidDateException ex,
            HttpServletRequest request) {

        log.warn("Invalid date input: {}", ex.getMessage());

        ErrorInfo error = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                environment.getProperty(ex.getMessage()),
                request.getRequestURI(),
                environment.getProperty("General.EXCEPTION_INVALID_PARAM")
        );

        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class}) 
	public ResponseEntity<ErrorInfo> exceptionHandler2(Exception ex, HttpServletRequest request) {

		String errorMsg;
		if (ex instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException manve = (MethodArgumentNotValidException) ex;
			errorMsg = manve.getBindingResult().getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(", "));
		} 
		else {
			ConstraintViolationException cve = (ConstraintViolationException) ex;
			errorMsg = cve.getConstraintViolations().stream().map(x -> x.getMessage()).collect(Collectors.joining(", "));
		}
		ErrorInfo error = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                errorMsg,
                request.getRequestURI(),
                environment.getProperty("General.EXCEPTION_INVALID_PARAM")
        );
		return ResponseEntity.badRequest().body(error);
	}
}