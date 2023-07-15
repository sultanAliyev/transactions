package com.earl.bank.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity<?> accountNotFoundException() {
        return new ResponseEntity<>("Account Not Found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidCurrencyException.class)
    public ResponseEntity<?> invalidCurrencyException() {
        return new ResponseEntity<>("Invalid Currency", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidAmountException.class)
    public ResponseEntity<?> invalidAmountException() {
        return new ResponseEntity<>("Invalid base amount", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = InsufficientFundException.class)
    public ResponseEntity<?> insufficientFundException() {
        return new ResponseEntity<>("Insufficient Fund", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<?> handleConverterErrors(JsonMappingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleConverterErrors() {
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> databaseConnectionFailsException(Exception exception) {
        logger.info(exception.getMessage());
        return new ResponseEntity<>("There is a problem", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
