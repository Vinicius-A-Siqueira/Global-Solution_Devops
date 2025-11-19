package com.wellmind.dto.usuario;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO para atualizar um usuário
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUsuarioDTO {

    @Size(min = 3, max = 100, message = "{validation.nome.size}")
    private String nome;

    @Size(min = 11, max = 20, message = "{validation.telefone.size}")
    private String telefone;

    @Size(min = 8, max = 255, message = "{validation.senha.size}")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).*$",
            message = "{validation.senha.pattern}")
    private String novaSenha;
}
