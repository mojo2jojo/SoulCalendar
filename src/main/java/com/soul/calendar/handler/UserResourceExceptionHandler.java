package com.soul.calendar.handler;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.soul.calendar.exception.UserErrorResponse;
import com.soul.calendar.exception.UserNotFoundException;

@ControllerAdvice
public class UserResourceExceptionHandler {
    public UserResourceExceptionHandler() {
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> NotFoundExceptionHandler(UserNotFoundException ex) {

        UserErrorResponse error = new UserErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(ex.getMessage().toString());
        error.setTimestamp(new Timestamp(System.currentTimeMillis()));

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
