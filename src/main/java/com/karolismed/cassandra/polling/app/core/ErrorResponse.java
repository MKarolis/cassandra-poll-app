package com.karolismed.cassandra.polling.app.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Object error;

    public ErrorResponse(HttpStatus status, String message, Object error) {
        this.status = status.value();
        this.message = message;
        this.error = error;
    }

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }
}
