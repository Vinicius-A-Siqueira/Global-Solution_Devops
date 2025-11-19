package com.wellmind.dto.profissionalsaude;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfissionalSaudeDTO {

    private Long idProfissional;
    private String nome;
    private String especialidade;
    private String crpCrm;
    private String tipoRegistro;
    private String email;
    private String telefone;
    private Boolean disponivel;
    private String horarioAtendimento;
    private Double valorConsulta;
    private String valorConsultaFormatado;
    private String descricao;
    private String fotoUrl;
    private Boolean ativo;
}
