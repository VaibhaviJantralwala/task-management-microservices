package com.spring.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.spring.payload.ApiResponse;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	// any exception other than listed below - 500 + Error logs
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Map>> handleGenericExceptions(Exception exception) {
		
		log.error("Unhandled Exception",exception);
		
		ApiResponse<Map> apiResponse = new ApiResponse<>("Something went Wrong!","ERROR",Collections.emptyMap());
		
		return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//validation fails - 400
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException exception) {
		
		Map<String, String> map = new HashMap<>();
		
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		for( FieldError error : fieldErrors ) {
			map.put(error.getField(), error.getDefaultMessage());
		}
		
		ApiResponse<Map<String,String>> apiResponse = new ApiResponse<>("Validation Failed", "ERROR", map);
		
		return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
	}
	
	// duplicate username or password - 409
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiResponse<Map>> handleDuplicateExceptions(DataIntegrityViolationException exception) {
		
		ApiResponse<Map> apiResponse = new ApiResponse<>("Username or Email already Exists!", "ERROR", Collections.emptyMap());
		
		return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
	}
	
	// resource not found( id not found) - 404
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<Map<Object, Object>>> handleNotFound(ResourceNotFoundException exception) {
		
		ApiResponse<Map<Object,Object>> apiResponse = new ApiResponse<>(exception.getMessage(), "ERROR", Collections.emptyMap());
		
		return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(FeignException.class)
	public ResponseEntity<ApiResponse<Map<Object, Object>>> handleFeignException(FeignException exception) {
		
		log.error("Feign call Failed!",exception);
		
		HttpStatus status = HttpStatus.resolve(exception.status());
		if (status == null) {
			status = HttpStatus.SERVICE_UNAVAILABLE;  // -1 matlab service tak pahunche hi nahi
		}
		
		String message = (status == HttpStatus.SERVICE_UNAVAILABLE)
				? "A required service is currently unavailable. Please try again later."
				: "A dependent service returned an error.";
		
		ApiResponse<Map<Object, Object>> response = new ApiResponse<>(message, "ERROR", Collections.emptyMap());

		return new ResponseEntity<>(response, status);
	}
	
	
}
