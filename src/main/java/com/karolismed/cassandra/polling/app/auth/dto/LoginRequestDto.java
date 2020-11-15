package com.karolismed.cassandra.polling.app.auth.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class LoginRequestDto {
    @NotEmpty(message = "Username is required")
    @NotEmpty
    private String username;
    @NotEmpty(message = "Password is required")
    private String password;
}
