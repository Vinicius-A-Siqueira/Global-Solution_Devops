package com.wellmind.dto.usuarioempresa;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEmpresaDTO {

    private Long idUsuarioEmpresa;
    private Long idUsuario;
    private String nomeUsuario;
    private Long idEmpresa;
    private String nomeEmpresa;
    private String cargo;
    private LocalDate dataVinculo;
    private LocalDate dataDesvinculo;
    private String statusVinculo;
    private Integer tempoVinculoAnos;
    private Boolean ativo;
}
