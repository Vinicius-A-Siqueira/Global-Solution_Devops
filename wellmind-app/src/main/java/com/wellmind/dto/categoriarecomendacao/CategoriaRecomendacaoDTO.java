package com.wellmind.dto.categoriarecomendacao;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaRecomendacaoDTO {

    private Long idCategoria;
    private String nomeCategoria;
    private String descricao;
    private String icone;
    private Integer ordemExibicao;
    private Boolean ativa;
}
