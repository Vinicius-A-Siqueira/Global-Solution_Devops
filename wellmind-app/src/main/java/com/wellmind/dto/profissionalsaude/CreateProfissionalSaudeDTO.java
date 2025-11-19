package com.wellmind.dto.profissionalsaude;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO para criar um profissional de saúde
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProfissionalSaudeDTO {

    @NotBlank(message = "{validation.nome.not.blank}")
    @Size(min = 3, max = 100, message = "{validation.nome.size}")
    private String nome;

    @NotBlank(message = "{validation.especialidade.not.blank}")
    @Size(min = 3, max = 100, message = "{validation.especialidade.size}")
    private String especialidade;

    @NotBlank(message = "{validation.crpcrm.not.blank}")
    @Size(min = 8, max = 20, message = "{validation.crpcrm.size}")
    @Pattern(regexp = "^(CRP|CRM)\\d{6}/[A-Z]{2}$", message = "{validation.crpcrm.pattern}")
    private String crpCrm;

    @Email(message = "{validation.email.invalid}")
    private String email;

    @Size(max = 20, message = "{validation.telefone.size}")
    private String telefone;

    @Size(max = 200, message = "{validation.horario.size}")
    private String horarioAtendimento;

    @DecimalMin(value = "0.0", inclusive = false, message = "{validation.valor.min}")
    @DecimalMax(value = "10000.0", message = "{validation.valor.max}")
    private Double valorConsulta;

    @Size(max = 1000, message = "{validation.descricao.size}")
    private String descricao;

    @Size(max = 500, message = "{validation.foto.size}")
    private String fotoUrl;
}
