package com.karolismed.cassandra.polling.app.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginResponseDto {
    private String jwt;
}
