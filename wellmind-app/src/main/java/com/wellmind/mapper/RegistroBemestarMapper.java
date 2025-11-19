package com.wellmind.mapper;

import com.wellmind.dto.registrobemestar.*;
import com.wellmind.entity.RegistroBemestar;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper para converter RegistroBemestar ↔ DTOs
 */
@Component
public class RegistroBemestarMapper {

    public RegistroBemestarDTO toDTO(RegistroBemestar registro) {
        if (registro == null) return null;

        List<String> motivosAlerta = new ArrayList<>();
        boolean temAlerta = false;

        if (registro.isEstresseAlto()) {
            motivosAlerta.add("Nível de estresse alto (>= 8)");
            temAlerta = true;
        }
        if (registro.isHumorBaixo()) {
            motivosAlerta.add("Humor baixo (<= 3)");
            temAlerta = true;
        }
        if (registro.isSonoInadequado()) {
            motivosAlerta.add("Sono inadequado (< 6h ou > 9h)");
            temAlerta = true;
        }

        return RegistroBemestarDTO.builder()
                .idRegistro(registro.getIdRegistro())
                .idUsuario(registro.getUsuario().getIdUsuario())
                .nomeUsuario(registro.getUsuario().getNome())
                .nivelHumor(registro.getNivelHumor())
                .nivelEstresse(registro.getNivelEstresse())
                .nivelEnergia(registro.getNivelEnergia())
                .horasSono(registro.getHorasSono())
                .qualidadeSono(registro.getQualidadeSono())
                .mediaBemestar(registro.getMediaBemestar())
                .classificacao(registro.getClassificacaoBemestar())
                .observacoes(registro.getObservacoes())
                .dataRegistro(registro.getDataRegistro())
                .temAlerta(temAlerta)
                .motivoAlerta(motivosAlerta.isEmpty() ? null : String.join(", ", motivosAlerta))
                .build();
    }

    public RegistroBemestar toEntity(CreateRegistroBemestarDTO dto) {
        if (dto == null) return null;

        return RegistroBemestar.builder()
                .nivelHumor(dto.getNivelHumor())
                .nivelEstresse(dto.getNivelEstresse())
                .nivelEnergia(dto.getNivelEnergia())
                .horasSono(dto.getHorasSono())
                .qualidadeSono(dto.getQualidadeSono())
                .observacoes(dto.getObservacoes())
                .build();
    }
}
