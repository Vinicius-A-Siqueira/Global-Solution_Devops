package com.wellmind.dto.auth;

import lombok.*;

/**
 * DTO para resposta de autenticação
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {

    private String token;
    private String tokenType;
    private Long expiresIn;
    private String usuario;
    private String email;
    private String mensagem;
}
