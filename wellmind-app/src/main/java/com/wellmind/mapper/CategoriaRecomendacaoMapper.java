package com.wellmind.mapper;

import com.wellmind.dto.categoriarecomendacao.*;
import com.wellmind.entity.CategoriaRecomendacao;
import org.springframework.stereotype.Component;

/**
 * Mapper para converter CategoriaRecomendacao ↔ DTOs
 */
@Component
public class CategoriaRecomendacaoMapper {

    public CategoriaRecomendacaoDTO toDTO(CategoriaRecomendacao categoria) {
        if (categoria == null) return null;

        return CategoriaRecomendacaoDTO.builder()
                .idCategoria(categoria.getIdCategoria())
                .nomeCategoria(categoria.getNomeCategoria())
                .descricao(categoria.getDescricao())
                .icone(categoria.getIcone())
                .ordemExibicao(categoria.getOrdemExibicao())
                .ativa("S".equals(categoria.getStatusAtivo()))
                .build();
    }

    public CategoriaRecomendacao toEntity(CreateCategoriaRecomendacaoDTO dto) {
        if (dto == null) return null;

        return CategoriaRecomendacao.builder()
                .nomeCategoria(dto.getNomeCategoria())
                .descricao(dto.getDescricao())
                .icone(dto.getIcone())
                .ordemExibicao(dto.getOrdemExibicao())
                .statusAtivo("S")
                .build();
    }
}
