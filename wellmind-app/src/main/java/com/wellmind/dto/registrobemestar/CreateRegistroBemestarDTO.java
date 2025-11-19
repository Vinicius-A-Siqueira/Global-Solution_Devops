package com.wellmind.dto.registrobemestar;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO para criar um registro de bem-estar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRegistroBemestarDTO {

    @NotNull(message = "{validation.usuario.not.null}")
    private Long idUsuario;

    @NotNull(message = "{validation.nivelhumor.not.null}")
    @Min(value = 1, message = "{validation.nivel.min}")
    @Max(value = 10, message = "{validation.nivel.max}")
    private Integer nivelHumor;

    @NotNull(message = "{validation.nivelestresse.not.null}")
    @Min(value = 1, message = "{validation.nivel.min}")
    @Max(value = 10, message = "{validation.nivel.max}")
    private Integer nivelEstresse;

    @NotNull(message = "{validation.nivelenergia.not.null}")
    @Min(value = 1, message = "{validation.nivel.min}")
    @Max(value = 10, message = "{validation.nivel.max}")
    private Integer nivelEnergia;

    @Min(value = 0, message = "{validation.sono.min}")
    @Max(value = 24, message = "{validation.sono.max}")
    private Double horasSono;

    @Min(value = 1, message = "{validation.nivel.min}")
    @Max(value = 10, message = "{validation.nivel.max}")
    private Integer qualidadeSono;

    @Size(max = 1000, message = "{validation.observacoes.size}")
    private String observacoes;
}
