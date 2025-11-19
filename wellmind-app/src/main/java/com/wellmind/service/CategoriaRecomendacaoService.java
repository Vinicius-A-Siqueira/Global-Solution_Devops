package com.wellmind.service;

import com.wellmind.dto.categoriarecomendacao.*;
import com.wellmind.entity.CategoriaRecomendacao;
import com.wellmind.exception.ResourceNotFoundException;
import com.wellmind.exception.ResourceAlreadyExistsException;
import com.wellmind.mapper.CategoriaRecomendacaoMapper;
import com.wellmind.repository.CategoriaRecomendacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para gerenciar Categorias de Recomendação
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoriaRecomendacaoService {

    private final CategoriaRecomendacaoRepository categoriaRepository;
    private final CategoriaRecomendacaoMapper categoriaMapper;

    /**
     * Lista todas as categorias ativas (com cache)
     */
    @Cacheable(value = "categorias")
    public List<CategoriaRecomendacaoDTO> listarAtivas() {
        log.info("Listando categorias ativas");

        return categoriaRepository.findAllActiveOrdered()
                .stream()
                .map(categoriaMapper::toDTO)
                .toList();
    }

    /**
     * Busca categoria por ID
     */
    @Cacheable(value = "categorias", key = "#id")
    public CategoriaRecomendacaoDTO buscarPorId(Long id) {
        log.info("Buscando categoria com ID: {}", id);

        CategoriaRecomendacao categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        return categoriaMapper.toDTO(categoria);
    }

    /**
     * Busca categoria por nome
     */
    public CategoriaRecomendacaoDTO buscarPorNome(String nome) {
        log.info("Buscando categoria com nome: {}", nome);

        CategoriaRecomendacao categoria = categoriaRepository.findByNomeCategoriaAndActive(nome)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        return categoriaMapper.toDTO(categoria);
    }

    /**
     * Cria nova categoria
     */
    @CacheEvict(value = "categorias", allEntries = true)
    public CategoriaRecomendacaoDTO criar(CreateCategoriaRecomendacaoDTO dto) {
        log.info("Criando nova categoria: {}", dto.getNomeCategoria());

        // Verificar duplicação
        if (categoriaRepository.existsByNomeCategoria(dto.getNomeCategoria())) {
            throw new ResourceAlreadyExistsException(
                    "Categoria com este nome já existe"
            );
        }

        CategoriaRecomendacao categoria = categoriaMapper.toEntity(dto);
        categoria.setStatusAtivo("S");

        CategoriaRecomendacao categoriaSalva = categoriaRepository.save(categoria);
        log.info("Categoria criada com sucesso - ID: {}", categoriaSalva.getIdCategoria());

        return categoriaMapper.toDTO(categoriaSalva);
    }

    /**
     * Atualiza uma categoria
     */
    @CacheEvict(value = "categorias", allEntries = true)
    public CategoriaRecomendacaoDTO atualizar(Long id, CreateCategoriaRecomendacaoDTO dto) {
        log.info("Atualizando categoria com ID: {}", id);

        CategoriaRecomendacao categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        categoria.setNomeCategoria(dto.getNomeCategoria());
        categoria.setDescricao(dto.getDescricao());
        categoria.setIcone(dto.getIcone());
        categoria.setOrdemExibicao(dto.getOrdemExibicao());

        CategoriaRecomendacao categoriaAtualizada = categoriaRepository.save(categoria);
        log.info("Categoria atualizada com sucesso");

        return categoriaMapper.toDTO(categoriaAtualizada);
    }

    /**
     * Desativa uma categoria
     */
    @CacheEvict(value = "categorias", allEntries = true)
    public void desativar(Long id) {
        log.info("Desativando categoria com ID: {}", id);

        CategoriaRecomendacao categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        categoria.desativar();
        categoriaRepository.save(categoria);

        log.info("Categoria desativada com sucesso");
    }
}
