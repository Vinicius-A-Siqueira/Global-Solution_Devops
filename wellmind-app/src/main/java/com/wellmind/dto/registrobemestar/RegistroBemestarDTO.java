package com.wellmind.dto.registrobemestar;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroBemestarDTO {

    private Long idRegistro;
    private Long idUsuario;
    private String nomeUsuario;
    private Integer nivelHumor;
    private Integer nivelEstresse;
    private Integer nivelEnergia;
    private Double horasSono;
    private Integer qualidadeSono;
    private Double mediaBemestar;
    private String classificacao;
    private String observacoes;
    private LocalDateTime dataRegistro;
    private Boolean temAlerta;
    private String motivoAlerta;
}
