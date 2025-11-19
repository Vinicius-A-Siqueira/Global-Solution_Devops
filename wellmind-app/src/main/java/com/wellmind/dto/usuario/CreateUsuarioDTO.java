package com.wellmind.dto.usuario;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO para criar um novo usuário
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUsuarioDTO {

    @NotBlank(message = "{validation.nome.not.blank}")
    @Size(min = 3, max = 100, message = "{validation.nome.size}")
    private String nome;

    @Email(message = "{validation.email.invalid}")
    @NotBlank(message = "{validation.email.not.blank}")
    private String email;

    @NotBlank(message = "{validation.senha.not.blank}")
    @Size(min = 8, max = 255, message = "{validation.senha.size}")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).*$",
            message = "{validation.senha.pattern}")
    private String senha;

    @NotNull(message = "{validation.datanascimento.not.null}")
    @PastOrPresent(message = "{validation.datanascimento.past}")
    private LocalDate dataNascimento;

    @NotBlank(message = "{validation.genero.not.blank}")
    private String genero; // Masculino, Feminino, Outro, Prefiro não informar

    @NotBlank(message = "{validation.telefone.not.blank}")
    @Size(min = 11, max = 20, message = "{validation.telefone.size}")
    private String telefone;
}
