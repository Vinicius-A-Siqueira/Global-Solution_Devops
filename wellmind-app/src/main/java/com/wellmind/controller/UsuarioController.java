package com.wellmind.controller;

import com.wellmind.dto.usuario.*;
import com.wellmind.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para Usuários
 *
 * Endpoints:
 * - GET /api/v1/usuario - Listar usuários
 * - GET /api/v1/usuario/{id} - Buscar por ID
 * - GET /api/v1/usuario/email/{email} - Buscar por email
 * - GET /api/v1/usuario/nome/{nome} - Buscar por nome
 * - POST /api/v1/usuario - Criar usuário
 * - PUT /api/v1/usuario/{id} - Atualizar usuário
 * - DELETE /api/v1/usuario/{id} - Desativar usuário
 */
@RestController
@RequestMapping("/api/v1/usuario")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Lista usuários ativos (paginado)
     * GET /api/v1/usuario?page=0&size=20
     */
    @GetMapping
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<UsuarioDTO>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Listando usuários - página: {}, tamanho: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioDTO> usuarios = usuarioService.listarAtivos(pageable);

        return ResponseEntity.ok(usuarios);
    }

    /**
     * Busca usuário por ID
     * GET /api/v1/usuario/123
     */
    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        log.info("Buscando usuário por ID: {}", id);

        UsuarioDTO usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Busca usuário por email
     * GET /api/v1/usuario/email/usuario@example.com
     */
    @GetMapping("/email/{email}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UsuarioDTO> buscarPorEmail(@PathVariable String email) {
        log.info("Buscando usuário por email: {}", email);

        UsuarioDTO usuario = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Busca usuários por nome
     * GET /api/v1/usuario/nome/João?page=0&size=10
     */
    @GetMapping("/nome/{nome}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<UsuarioDTO>> buscarPorNome(
            @PathVariable String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Buscando usuários com nome: {}", nome);

        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioDTO> usuarios = usuarioService.buscarPorNome(nome, pageable);

        return ResponseEntity.ok(usuarios);
    }

    /**
     * Cria novo usuário
     * POST /api/v1/usuario
     */
    @PostMapping
    public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody CreateUsuarioDTO dto) {
        log.info("Criando novo usuário com email: {}", dto.getEmail());

        UsuarioDTO usuario = usuarioService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    /**
     * Atualiza usuário existente
     * PUT /api/v1/usuario/123
     */
    @PutMapping("/{id}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UsuarioDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUsuarioDTO dto) {

        log.info("Atualizando usuário: {}", id);

        UsuarioDTO usuario = usuarioService.atualizar(id, dto);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Desativa usuário
     * DELETE /api/v1/usuario/123
     */
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        log.info("Desativando usuário: {}", id);

        usuarioService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Reativa usuário
     * PUT /api/v1/usuario/123/reativar
     */
    @PutMapping("/{id}/reativar")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> reativar(@PathVariable Long id) {
        log.info("Reativando usuário: {}", id);

        usuarioService.reativar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca usuários sem registros recentes
     * GET /api/v1/usuario/sem-registros/recentes
     */
    @GetMapping("/sem-registros/recentes")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> buscarSemRegistrosRecentes() {
        log.info("Buscando usuários sem registros recentes");

        List<UsuarioDTO> usuarios = usuarioService.buscarSemRegistrosRecentes();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Retorna total de usuários ativos
     * GET /api/v1/usuario/total/ativos
     */
    @GetMapping("/total/ativos")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> totalAtivos() {
        log.info("Contando usuários ativos");

        long total = usuarioService.contarAtivos();
        return ResponseEntity.ok(total);
    }
}
