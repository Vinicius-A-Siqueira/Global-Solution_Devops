package com.wellmind.dto.usuarioempresa;

import com.wellmind.dto.usuario.UsuarioDTO;
import com.wellmind.dto.empresa.EmpresaDTO;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO para resposta detalhada de vínculo usuário-empresa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEmpresaResponseDTO {

    private Long idUsuarioEmpresa;
    private UsuarioDTO usuario;
    private EmpresaDTO empresa;
    private String cargo;
    private LocalDate dataVinculo;
    private Integer tempoVinculoAnos;
    private Boolean ativo;
}
