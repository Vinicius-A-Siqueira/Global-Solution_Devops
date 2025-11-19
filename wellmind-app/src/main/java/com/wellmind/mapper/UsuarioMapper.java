package com.wellmind.mapper;

import com.wellmind.dto.usuario.*;
import com.wellmind.entity.Usuario;
import org.springframework.stereotype.Component;

/**
 * Mapper para converter Usuario ↔ DTOs
 */
@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;

        return UsuarioDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .dataNascimento(usuario.getDataNascimento())
                .genero(usuario.getGenero())
                .telefone(usuario.getTelefone())
                .statusAtivo(usuario.getStatusAtivo())
                .dataCadastro(usuario.getDataCadastro())
                .ultimaAtualizacao(usuario.getUltimaAtualizacao())
                .build();
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) return null;

        String empresaAtual = usuario.getEmpresas().stream()
                .filter(ue -> "A".equals(ue.getStatusVinculo()))
                .map(ue -> ue.getEmpresa().getNomeEmpresa())
                .findFirst()
                .orElse("Sem empresa");

        String cargoAtual = usuario.getEmpresas().stream()
                .filter(ue -> "A".equals(ue.getStatusVinculo()))
                .map(ue -> ue.getCargo())
                .findFirst()
                .orElse("N/A");

        int idade = java.time.LocalDate.now().getYear() - usuario.getDataNascimento().getYear();

        return UsuarioResponseDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .dataNascimento(usuario.getDataNascimento())
                .idade(idade)
                .genero(usuario.getGenero())
                .telefone(usuario.getTelefone())
                .ativo("S".equals(usuario.getStatusAtivo()))
                .dataCadastro(usuario.getDataCadastro())
                .ultimaAtualizacao(usuario.getUltimaAtualizacao())
                .empresaAtual(empresaAtual)
                .cargoAtual(cargoAtual)
                .build();
    }

    public Usuario toEntity(CreateUsuarioDTO dto) {
        if (dto == null) return null;

        return Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .dataNascimento(dto.getDataNascimento())
                .genero(dto.getGenero())
                .telefone(dto.getTelefone())
                .statusAtivo("S")
                .build();
    }
}
