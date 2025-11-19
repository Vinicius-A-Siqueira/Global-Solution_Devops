package com.wellmind.service;

import com.wellmind.dto.empresa.*;
import com.wellmind.entity.Empresa;
import com.wellmind.exception.ResourceNotFoundException;
import com.wellmind.exception.ResourceAlreadyExistsException;
import com.wellmind.mapper.EmpresaMapper;
import com.wellmind.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service para gerenciar Empresas
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper empresaMapper;

    /**
     * Busca todas as empresas ativas (com cache)
     */
    @Cacheable(value = "empresas", key = "#pageable.pageNumber")
    public Page<EmpresaDTO> listarAtivas(Pageable pageable) {
        log.info("Listando empresas ativas - página {}", pageable.getPageNumber());

        return empresaRepository.findAllActive(pageable)
                .map(empresaMapper::toDTO);
    }

    /**
     * Busca empresa por ID
     */
    @Cacheable(value = "empresas", key = "#id")
    public EmpresaDTO buscarPorId(Long id) {
        log.info("Buscando empresa com ID: {}", id);

        Empresa empresa = empresaRepository.findByIdAndActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com ID: " + id));

        return empresaMapper.toDTO(empresa);
    }

    /**
     * Busca empresa por CNPJ
     */
    public EmpresaDTO buscarPorCnpj(String cnpj) {
        log.info("Buscando empresa com CNPJ: {}", cnpj);

        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");

        Empresa empresa = empresaRepository.findByCnpjAndActive(cnpjLimpo)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com CNPJ: " + cnpj));

        return empresaMapper.toDTO(empresa);
    }

    /**
     * Busca empresas por nome
     */
    public Page<EmpresaDTO> buscarPorNome(String nome, Pageable pageable) {
        log.info("Buscando empresas com nome contendo: {}", nome);

        return empresaRepository.findByNomeContaining(nome, pageable)
                .map(empresaMapper::toDTO);
    }

    /**
     * Cria nova empresa
     */
    @CacheEvict(value = "empresas", allEntries = true)
    public EmpresaDTO criar(CreateEmpresaDTO dto) {
        log.info("Criando nova empresa com CNPJ: {}", dto.getCnpj());

        String cnpjLimpo = dto.getCnpj().replaceAll("[^0-9]", "");

        // Validar se CNPJ já existe
        if (empresaRepository.existsByCnpj(cnpjLimpo)) {
            throw new ResourceAlreadyExistsException(
                    "CNPJ já registrado: " + dto.getCnpj()
            );
        }

        // Validar formato CNPJ (14 dígitos)
        if (cnpjLimpo.length() != 14) {
            throw new IllegalArgumentException("CNPJ inválido - deve ter 14 dígitos");
        }

        Empresa empresa = empresaMapper.toEntity(dto);
        empresa.setCnpj(cnpjLimpo);
        empresa.setStatusAtivo("S");

        Empresa empresaSalva = empresaRepository.save(empresa);
        log.info("Empresa criada com sucesso - ID: {}", empresaSalva.getIdEmpresa());

        return empresaMapper.toDTO(empresaSalva);
    }

    /**
     * Atualiza uma empresa
     */
    @CacheEvict(value = "empresas", key = "#id")
    public EmpresaDTO atualizar(Long id, UpdateEmpresaDTO dto) {
        log.info("Atualizando empresa com ID: {}", id);

        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com ID: " + id));

        if (dto.getNomeEmpresa() != null) {
            empresa.setNomeEmpresa(dto.getNomeEmpresa());
        }

        if (dto.getEndereco() != null) {
            empresa.setEndereco(dto.getEndereco());
        }

        if (dto.getTelefone() != null) {
            empresa.setTelefone(dto.getTelefone());
        }

        if (dto.getEmailContato() != null) {
            empresa.setEmailContato(dto.getEmailContato());
        }

        Empresa empresaAtualizada = empresaRepository.save(empresa);
        log.info("Empresa atualizada com sucesso - ID: {}", id);

        return empresaMapper.toDTO(empresaAtualizada);
    }

    /**
     * Desativa uma empresa
     */
    @CacheEvict(value = "empresas", key = "#id")
    public void desativar(Long id) {
        log.info("Desativando empresa com ID: {}", id);

        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com ID: " + id));

        empresa.desativar();
        empresaRepository.save(empresa);

        log.info("Empresa desativada com sucesso - ID: {}", id);
    }

    /**
     * Busca empresas com mais colaboradores
     */
    public Page<EmpresaDTO> buscarComMaisColaboradores(Pageable pageable) {
        log.info("Buscando empresas com mais colaboradores");

        return empresaRepository.findEmpresasComMaisColaboradores(pageable)
                .map(empresaMapper::toDTO);
    }

    /**
     * Conta empresas ativas
     */
    public long contarAtivas() {
        return empresaRepository.countActiveEmpresas();
    }
}
