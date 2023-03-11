package com.betting.javabettingapi.controller;

import com.betting.javabettingapi.dto.ErrorDto;
import com.betting.javabettingapi.exception.EntityNotFoundException;
import com.betting.javabettingapi.exception.InsufficientFundsException;
import com.betting.javabettingapi.exception.UsernameTakenException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MvcExceptionHandler {
    private final HttpHeaders httpHeaders;

    public MvcExceptionHandler(){
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> entityNotFoundErrorHandler(EntityNotFoundException ex) {
        final ErrorDto error = new ErrorDto(ex.getMessage());

        return new ResponseEntity(error, httpHeaders, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorDto> insufficientFundsException(InsufficientFundsException ex) {
        final ErrorDto error = new ErrorDto(ex.getMessage());

        return new ResponseEntity(error, httpHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameTakenException.class)
    public ResponseEntity<ErrorDto> usernameTakenException(UsernameTakenException ex) {
        final ErrorDto error = new ErrorDto(ex.getMessage());

        return new ResponseEntity(error, httpHeaders, HttpStatus.CONFLICT);
    }
}
