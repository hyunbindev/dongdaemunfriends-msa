package com.baduk.baduk.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.baduk.baduk.data.CommonExceptionDTO;
import com.baduk.baduk.exception.CommonException;

@RestControllerAdvice
public class GlobalControllerAdvice {
	@ExceptionHandler(CommonException.class)
	public ResponseEntity<CommonExceptionDTO> handleCommonException(CommonException exception){
		return new ResponseEntity<CommonExceptionDTO>(new CommonExceptionDTO(exception),exception.getHttpStatus());
	}
}
