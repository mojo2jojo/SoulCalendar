package com.soul.calendar.handler;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.soul.calendar.exception.ErrorResponse;

@ControllerAdvice
public class AllExceptionHandler {
    
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(), new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
