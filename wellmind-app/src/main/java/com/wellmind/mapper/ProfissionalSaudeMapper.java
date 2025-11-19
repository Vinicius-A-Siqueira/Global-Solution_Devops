package com.wellmind.mapper;

import com.wellmind.dto.profissionalsaude.*;
import com.wellmind.entity.ProfissionalSaude;
import org.springframework.stereotype.Component;

/**
 * Mapper para converter ProfissionalSaude ↔ DTOs
 */
@Component
public class ProfissionalSaudeMapper {

    public ProfissionalSaudeDTO toDTO(ProfissionalSaude profissional) {
        if (profissional == null) return null;

        return ProfissionalSaudeDTO.builder()
                .idProfissional(profissional.getIdProfissional())
                .nome(profissional.getNome())
                .especialidade(profissional.getEspecialidade())
                .crpCrm(profissional.getCrpCrm())
                .tipoRegistro(profissional.getTipoRegistro())
                .email(profissional.getEmail())
                .telefone(profissional.getTelefone())
                .disponivel(profissional.isDisponivel())
                .horarioAtendimento(profissional.getHorarioAtendimento())
                .valorConsulta(profissional.getValorConsulta())
                .valorConsultaFormatado(profissional.getValorConsultaFormatado())
                .descricao(profissional.getDescricao())
                .fotoUrl(profissional.getFotoUrl())
                .ativo(profissional.isAtivo())
                .build();
    }

    public ProfissionalSaude toEntity(CreateProfissionalSaudeDTO dto) {
        if (dto == null) return null;

        return ProfissionalSaude.builder()
                .nome(dto.getNome())
                .especialidade(dto.getEspecialidade())
                .crpCrm(dto.getCrpCrm())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .horarioAtendimento(dto.getHorarioAtendimento())
                .valorConsulta(dto.getValorConsulta())
                .descricao(dto.getDescricao())
                .fotoUrl(dto.getFotoUrl())
                .statusAtivo("S")
                .disponivel("S")
                .build();
    }
}
