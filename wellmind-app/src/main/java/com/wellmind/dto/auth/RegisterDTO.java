package com.wellmind.dto.auth;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDTO {

    @NotBlank(message = "{validation.nome.not.blank}")
    @Size(min = 3, max = 100, message = "{validation.nome.size}")
    private String nome;

    @Email(message = "{validation.email.invalid}")
    @NotBlank(message = "{validation.email.not.blank}")
    private String email;

    @NotBlank(message = "{validation.senha.not.blank}")
    @Size(min = 8, message = "{validation.senha.size}")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).*$",
            message = "{validation.senha.pattern}")
    private String senha;

    @NotNull(message = "{validation.datanascimento.not.null}")
    @PastOrPresent(message = "{validation.datanascimento.past}")
    private LocalDate dataNascimento;

    @NotBlank(message = "{validation.genero.not.blank}")
    private String genero;

    @NotBlank(message = "{validation.telefone.not.blank}")
    @Size(min = 11, max = 20, message = "{validation.telefone.size}")
    private String telefone;
}
