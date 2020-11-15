package com.karolismed.cassandra.polling.app.auth.service;

import com.karolismed.cassandra.polling.app.auth.dto.LoginRequestDto;
import com.karolismed.cassandra.polling.app.auth.dto.LoginResponseDto;
import com.karolismed.cassandra.polling.app.config.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public LoginResponseDto authenticate(LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return LoginResponseDto.builder()
            .jwt(jwt)
            .build();
    }
}
