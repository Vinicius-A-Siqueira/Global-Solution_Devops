package com.wellmind.dto.categoriarecomendacao;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoriaRecomendacaoDTO {

    @NotBlank(message = "{validation.nome.not.blank}")
    @Size(min = 3, max = 100, message = "{validation.nome.size}")
    private String nomeCategoria;

    @Size(max = 500, message = "{validation.descricao.size}")
    private String descricao;

    @Size(max = 50, message = "{validation.icone.size}")
    private String icone;

    @Min(value = 1, message = "{validation.ordem.min}")
    private Integer ordemExibicao;
}
