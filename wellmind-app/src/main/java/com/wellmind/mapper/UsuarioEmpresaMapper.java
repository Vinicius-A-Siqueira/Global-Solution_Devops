package com.wellmind.mapper;

import com.wellmind.dto.usuarioempresa.*;
import com.wellmind.entity.UsuarioEmpresa;
import org.springframework.stereotype.Component;

/**
 * Mapper para converter UsuarioEmpresa ↔ DTOs
 */
@Component
public class UsuarioEmpresaMapper {

    public UsuarioEmpresaDTO toDTO(UsuarioEmpresa vinculo) {
        if (vinculo == null) return null;

        return UsuarioEmpresaDTO.builder()
                .idUsuarioEmpresa(vinculo.getIdUsuarioEmpresa())
                .idUsuario(vinculo.getUsuario().getIdUsuario())
                .nomeUsuario(vinculo.getUsuario().getNome())
                .idEmpresa(vinculo.getEmpresa().getIdEmpresa())
                .nomeEmpresa(vinculo.getEmpresa().getNomeEmpresa())
                .cargo(vinculo.getCargo())
                .dataVinculo(vinculo.getDataVinculo())
                .dataDesvinculo(vinculo.getDataDesvinculo())
                .statusVinculo(vinculo.getStatusVinculo())
                .tempoVinculoAnos(vinculo.getTempoVinculoAnos())
                .ativo("A".equals(vinculo.getStatusVinculo()))
                .build();
    }

    public UsuarioEmpresa toEntity(CreateUsuarioEmpresaDTO dto) {
        if (dto == null) return null;

        return UsuarioEmpresa.builder()
                .cargo(dto.getCargo())
                .dataVinculo(dto.getDataVinculo())
                .statusVinculo("A")
                .build();
    }
}
