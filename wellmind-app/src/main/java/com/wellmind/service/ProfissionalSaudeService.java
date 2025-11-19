package com.wellmind.service;

import com.wellmind.dto.profissionalsaude.*;
import com.wellmind.entity.ProfissionalSaude;
import com.wellmind.exception.ResourceNotFoundException;
import com.wellmind.exception.ResourceAlreadyExistsException;
import com.wellmind.mapper.ProfissionalSaudeMapper;
import com.wellmind.repository.ProfissionalSaudeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para gerenciar Profissionais de Saúde
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProfissionalSaudeService {

    private final ProfissionalSaudeRepository profissionalRepository;
    private final ProfissionalSaudeMapper profissionalMapper;

    /**
     * Busca profissionais disponíveis (com cache)
     */
    @Cacheable(value = "profissionais", key = "#pageable.pageNumber")
    public Page<ProfissionalSaudeDTO> listarDisponíveis(Pageable pageable) {
        log.info("Listando profissionais disponíveis");

        return profissionalRepository.findAllAvailable(pageable)
                .map(profissionalMapper::toDTO);
    }

    /**
     * Busca profissional por ID
     */
    @Cacheable(value = "profissionais", key = "#id")
    public ProfissionalSaudeDTO buscarPorId(Long id) {
        log.info("Buscando profissional com ID: {}", id);

        ProfissionalSaude profissional = profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));

        return profissionalMapper.toDTO(profissional);
    }

    /**
     * Busca profissionais por especialidade
     */
    public Page<ProfissionalSaudeDTO> buscarPorEspecialidade(String especialidade, Pageable pageable) {
        log.info("Buscando profissionais da especialidade: {}", especialidade);

        return profissionalRepository.findByEspecialidade(especialidade, pageable)
                .map(profissionalMapper::toDTO);
    }

    /**
     * Busca profissionais por nome
     */
    public Page<ProfissionalSaudeDTO> buscarPorNome(String nome, Pageable pageable) {
        log.info("Buscando profissionais com nome: {}", nome);

        return profissionalRepository.findByNomeContaining(nome, pageable)
                .map(profissionalMapper::toDTO);
    }

    /**
     * Lista todas as especialidades
     */
    @Cacheable(value = "especialidades")
    public List<String> listarEspecialidades() {
        log.info("Listando especialidades disponíveis");

        return profissionalRepository.findAllEspecialidades();
    }

    /**
     * Cria novo profissional
     */
    @CacheEvict(value = {"profissionais", "especialidades"}, allEntries = true)
    public ProfissionalSaudeDTO criar(CreateProfissionalSaudeDTO dto) {
        log.info("Criando novo profissional: {}", dto.getNome());

        // Validar duplicação de CRP/CRM
        if (profissionalRepository.existsByCrpCrm(dto.getCrpCrm())) {
            throw new ResourceAlreadyExistsException(
                    "Profissional com este registro já existe"
            );
        }

        ProfissionalSaude profissional = profissionalMapper.toEntity(dto);
        profissional.setStatusAtivo("S");
        profissional.setDisponivel("S");

        ProfissionalSaude profissionalSalvo = profissionalRepository.save(profissional);
        log.info("Profissional criado com sucesso - ID: {}", profissionalSalvo.getIdProfissional());

        return profissionalMapper.toDTO(profissionalSalvo);
    }

    /**
     * Atualiza disponibilidade de um profissional
     */
    @CacheEvict(value = "profissionais", allEntries = true)
    public ProfissionalSaudeDTO atualizarDisponibilidade(Long id, Boolean disponivel) {
        log.info("Atualizando disponibilidade do profissional: {}", id);

        ProfissionalSaude profissional = profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));

        if (disponivel) {
            profissional.marcarDisponivel();
        } else {
            profissional.marcarIndisponivel();
        }

        ProfissionalSaude profissionalAtualizado = profissionalRepository.save(profissional);
        log.info("Disponibilidade atualizada com sucesso");

        return profissionalMapper.toDTO(profissionalAtualizado);
    }

    /**
     * Desativa um profissional
     */
    @CacheEvict(value = "profissionais", allEntries = true)
    public void desativar(Long id) {
        log.info("Desativando profissional com ID: {}", id);

        ProfissionalSaude profissional = profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));

        profissional.desativar();
        profissionalRepository.save(profissional);

        log.info("Profissional desativado com sucesso");
    }

    /**
     * Busca profissionais por faixa de preço
     */
    public List<ProfissionalSaudeDTO> buscarPorFaixaPreco(Double min, Double max) {
        log.info("Buscando profissionais com preço entre {} e {}", min, max);

        return profissionalRepository.findByFaixaPreco(min, max)
                .stream()
                .map(profissionalMapper::toDTO)
                .toList();
    }

    /**
     * Conta profissionais disponíveis
     */
    public long contarDisponíveis() {
        return profissionalRepository.countAvailable();
    }
}
