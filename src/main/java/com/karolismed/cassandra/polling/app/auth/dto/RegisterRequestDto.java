package com.karolismed.cassandra.polling.app.auth.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class RegisterRequestDto {
    @NotEmpty
    @Size(min = 3, max = 120, message = "Username's length must be between 3 and 120")
    private String username;
    @NotEmpty
    @Size(min = 3, max = 120, message = "Password's length must be between 3 and 120")
    private String password;
}
