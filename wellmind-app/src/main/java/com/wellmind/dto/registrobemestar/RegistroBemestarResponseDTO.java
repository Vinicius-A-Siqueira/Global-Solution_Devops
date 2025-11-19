package com.wellmind.dto.registrobemestar;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para resposta detalhada de registro de bem-estar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroBemestarResponseDTO {

    private Long idRegistro;
    private String nomeUsuario;
    private Integer nivelHumor;
    private Integer nivelEstresse;
    private Integer nivelEnergia;
    private Double horasSono;
    private Integer qualidadeSono;
    private Double mediaBemestar;
    private String classificacao; // EXCELENTE, BOM, REGULAR, RUIM, CRÍTICO
    private String observacoes;
    private LocalDateTime dataRegistro;
    private Boolean alertasAtivos;
    private List<String> motivosAlerta;
}
