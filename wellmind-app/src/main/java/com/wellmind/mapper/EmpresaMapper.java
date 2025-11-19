package com.wellmind.mapper;

import com.wellmind.dto.empresa.*;
import com.wellmind.entity.Empresa;
import org.springframework.stereotype.Component;

/**
 * Mapper para converter Empresa ↔ DTOs
 */
@Component
public class EmpresaMapper {

    public EmpresaDTO toDTO(Empresa empresa) {
        if (empresa == null) return null;

        String cnpjFormatado = empresa.getCnpjFormatado();

        return EmpresaDTO.builder()
                .idEmpresa(empresa.getIdEmpresa())
                .nomeEmpresa(empresa.getNomeEmpresa())
                .cnpj(empresa.getCnpj())
                .cnpjFormatado(cnpjFormatado)
                .endereco(empresa.getEndereco())
                .telefone(empresa.getTelefone())
                .emailContato(empresa.getEmailContato())
                .ativa("S".equals(empresa.getStatusAtivo()))
                .dataCadastro(empresa.getDataCadastro())
                .ultimaAtualizacao(empresa.getUltimaAtualizacao())
                .totalColaboradores(empresa.getTotalColaboradoresAtivos())
                .build();
    }

    public Empresa toEntity(CreateEmpresaDTO dto) {
        if (dto == null) return null;

        String cnpj = dto.getCnpj().replaceAll("[^0-9]", "");

        return Empresa.builder()
                .nomeEmpresa(dto.getNomeEmpresa())
                .cnpj(cnpj)
                .endereco(dto.getEndereco())
                .telefone(dto.getTelefone())
                .emailContato(dto.getEmailContato())
                .statusAtivo("S")
                .build();
    }
}
