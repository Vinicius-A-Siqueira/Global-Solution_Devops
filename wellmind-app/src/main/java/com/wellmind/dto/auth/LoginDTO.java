package com.wellmind.dto.auth;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO para login
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDTO {

    @Email(message = "{validation.email.invalid}")
    @NotBlank(message = "{validation.email.not.blank}")
    private String email;

    @NotBlank(message = "{validation.senha.not.blank}")
    private String senha;
}
