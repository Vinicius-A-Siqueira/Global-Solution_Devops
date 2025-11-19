package com.wellmind.dto.usuario;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private Long idUsuario;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private Integer idade;
    private String genero;
    private String telefone;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
    private LocalDateTime ultimaAtualizacao;
    private String empresaAtual;
    private String cargoAtual;
}
