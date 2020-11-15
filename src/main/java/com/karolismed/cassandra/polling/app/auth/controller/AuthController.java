package com.karolismed.cassandra.polling.app.auth.controller;

import com.karolismed.cassandra.polling.app.auth.dto.LoginRequestDto;
import com.karolismed.cassandra.polling.app.auth.dto.LoginResponseDto;
import com.karolismed.cassandra.polling.app.auth.dto.RegisterRequestDto;
import com.karolismed.cassandra.polling.app.auth.service.AuthService;
import com.karolismed.cassandra.polling.app.auth.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userDetailsService;
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        userDetailsService.registerUser(registerRequestDto);
        return "Hi!";
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return authService.authenticate(loginRequestDto);
    }
}
