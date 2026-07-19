package com.enterprise.cart.controller;

import com.enterprise.cart.dto.LoginRequest;
import com.enterprise.cart.dto.LoginResponse;
import com.enterprise.cart.exception.InvalidCredentialsException;
import com.enterprise.cart.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /api/auth/login username='{}'", request.getUsername());
        return ResponseEntity.ok(authService.login(request.getUsername(), request.getPassword()));
    }

    /**
     * Lets the frontend confirm a stored token is still valid on reload.
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization) {
        String username = authService.resolveToken(stripBearer(authorization))
                .orElseThrow(InvalidCredentialsException::new);
        return ResponseEntity.ok(Map.of("username", username));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization) {
        authService.logout(stripBearer(authorization));
        return ResponseEntity.noContent().build();
    }

    private String stripBearer(String authorization) {
        if (authorization == null) return null;
        return authorization.startsWith("Bearer ") ? authorization.substring(7) : authorization;
    }
}
