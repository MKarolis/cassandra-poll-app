package com.karolismed.cassandra.polling.app.core;

import com.karolismed.cassandra.polling.app.core.exception.BadRequestException;
import com.karolismed.cassandra.polling.app.core.exception.ConflictException;
import com.karolismed.cassandra.polling.app.core.exception.ForbiddenException;
import com.karolismed.cassandra.polling.app.core.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({ConflictException.class})
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage()),
            HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex) {
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.FORBIDDEN, "Forbidden", ex.getMessage()),
            HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage()),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST, "Request body missing"),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex
    ) {
        List<String> details = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }

        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", details),
            HttpStatus.BAD_REQUEST
        );
    }
}
