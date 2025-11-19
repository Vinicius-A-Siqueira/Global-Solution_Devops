package com.wellmind.controller;

import com.wellmind.dto.auth.*;
import com.wellmind.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para Autenticação
 *
 * Endpoints:
 * - POST /api/v1/auth/login
 * - POST /api/v1/auth/register
 * - POST /api/v1/auth/refresh
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Login do usuário
     * POST /api/v1/auth/login
     *
     * @param dto LoginDTO com email e senha
     * @return AuthResponseDTO com token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        AuthResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Registro de novo usuário
     * POST /api/v1/auth/register
     *
     * @param dto RegisterDTO com dados do novo usuário
     * @return AuthResponseDTO com token JWT
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterDTO dto) {
        AuthResponseDTO response = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Refresh do token JWT
     * POST /api/v1/auth/refresh
     *
     * @return AuthResponseDTO com novo token
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestHeader("Authorization") String token) {
        AuthResponseDTO response = authService.refreshToken(token);
        return ResponseEntity.ok(response);
    }
}
