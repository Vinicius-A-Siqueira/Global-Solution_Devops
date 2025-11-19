package com.wellmind.dto.usuario;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para representar um usuário (Response)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    private Long idUsuario;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String genero;
    private String telefone;
    private String statusAtivo;
    private LocalDateTime dataCadastro;
    private LocalDateTime ultimaAtualizacao;
}
