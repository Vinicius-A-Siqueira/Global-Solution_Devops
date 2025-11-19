package com.wellmind.controller;

import com.wellmind.dto.categoriarecomendacao.*;
import com.wellmind.service.CategoriaRecomendacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para Categorias de Recomendação
 */
@RestController
@RequestMapping("/api/v1/categoria")
@RequiredArgsConstructor
@Slf4j
public class CategoriaRecomendacaoController {

    private final CategoriaRecomendacaoService categoriaService;

    /**
     * Lista todas as categorias ativas
     * GET /api/v1/categoria
     */
    @GetMapping
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<CategoriaRecomendacaoDTO>> listar() {
        log.info("Listando categorias ativas");

        List<CategoriaRecomendacaoDTO> categorias = categoriaService.listarAtivas();
        return ResponseEntity.ok(categorias);
    }

    /**
     * Busca categoria por ID
     * GET /api/v1/categoria/123
     */
    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CategoriaRecomendacaoDTO> buscarPorId(@PathVariable Long id) {
        log.info("Buscando categoria por ID: {}", id);

        CategoriaRecomendacaoDTO categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(categoria);
    }

    /**
     * Busca categoria por nome
     * GET /api/v1/categoria/nome/Meditação
     */
    @GetMapping("/nome/{nome}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CategoriaRecomendacaoDTO> buscarPorNome(@PathVariable String nome) {
        log.info("Buscando categoria por nome: {}", nome);

        CategoriaRecomendacaoDTO categoria = categoriaService.buscarPorNome(nome);
        return ResponseEntity.ok(categoria);
    }

    /**
     * Cria nova categoria
     * POST /api/v1/categoria
     */
    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaRecomendacaoDTO> criar(@Valid @RequestBody CreateCategoriaRecomendacaoDTO dto) {
        log.info("Criando nova categoria: {}", dto.getNomeCategoria());

        CategoriaRecomendacaoDTO categoria = categoriaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
    }

    /**
     * Atualiza categoria
     * PUT /api/v1/categoria/123
     */
    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaRecomendacaoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CreateCategoriaRecomendacaoDTO dto) {

        log.info("Atualizando categoria: {}", id);

        CategoriaRecomendacaoDTO categoria = categoriaService.atualizar(id, dto);
        return ResponseEntity.ok(categoria);
    }

    /**
     * Desativa categoria
     * DELETE /api/v1/categoria/123
     */
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        log.info("Desativando categoria: {}", id);

        categoriaService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
