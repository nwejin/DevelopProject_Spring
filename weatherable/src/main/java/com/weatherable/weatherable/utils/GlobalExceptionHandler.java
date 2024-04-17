package com.weatherable.weatherable.utils;

import com.weatherable.weatherable.enums.DefaultRes;
import com.weatherable.weatherable.enums.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultRes<String>> handleException(
            Exception e
    ){
        return new ResponseEntity<>(
                DefaultRes.res(StatusCode.UNAUTHORIZED, e.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

}