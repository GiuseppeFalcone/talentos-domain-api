package com.certimetergroup.easycv.domainapi.controller;

import com.certimetergroup.easycv.commons.enums.ResponseEnum;
import com.certimetergroup.easycv.commons.exception.ServerSideError;
import com.certimetergroup.easycv.commons.response.PayloadResponse;
import com.certimetergroup.easycv.commons.response.Response;
import com.certimetergroup.easycv.domainapi.exception.DomainNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.JDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler({ Exception.class, ServerSideError.class })
    public ResponseEntity<Response> handleGenericException(Exception exception) {
        return ResponseEntity.status(ResponseEnum.INTERNAL_SERVER_ERROR.code)
                .body(new Response(ResponseEnum.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler({ DomainNotFoundException.class })
    public ResponseEntity<Response> handleDomainNotFound(DomainNotFoundException exception) {
        return ResponseEntity.status(exception.getResponseEnum().httpStatus)
                .body(new Response(exception.getResponseEnum()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleRequestBody(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PayloadResponse<>(ResponseEnum.BAD_REQUEST, errors));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Response> handleBindException(BindException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PayloadResponse<>(ResponseEnum.BAD_REQUEST, errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response> handleConstraint(ConstraintViolationException exception) {
        Map<String,String> errors = new HashMap<>();
        exception.getConstraintViolations().forEach(violation -> {
            String path = violation.getPropertyPath().toString();
            errors.put(path, violation.getMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PayloadResponse<>(ResponseEnum.BAD_REQUEST, errors));
    }

    @ExceptionHandler({JDBCException.class, JDBCConnectionException.class})
    public ResponseEntity<Response> handleJDBCException(Exception exception) {
        return ResponseEntity.status(ResponseEnum.DATABASE_ERROR.code).body(new Response(ResponseEnum.INTERNAL_SERVER_ERROR));
    }
}
