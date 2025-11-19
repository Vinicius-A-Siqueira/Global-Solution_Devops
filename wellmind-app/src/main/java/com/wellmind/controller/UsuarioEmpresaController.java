package com.wellmind.controller;

import com.wellmind.dto.usuarioempresa.*;
import com.wellmind.service.UsuarioEmpresaService;
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
 * Controller para Vínculos Usuário-Empresa
 *
 * Endpoints:
 * - GET /api/v1/vinculo/usuario/{usuarioId} - Vínculos do usuário
 * - GET /api/v1/vinculo/empresa/{empresaId} - Colaboradores da empresa
 * - POST /api/v1/vinculo - Criar vínculo
 * - PUT /api/v1/vinculo/{id}/desativar - Desativar vínculo
 * - PUT /api/v1/vinculo/{id}/reativar - Reativar vínculo
 * - PUT /api/v1/vinculo/{id}/cargo - Atualizar cargo
 */
@RestController
@RequestMapping("/api/v1/vinculo")
@RequiredArgsConstructor
@Slf4j
public class UsuarioEmpresaController {

    private final UsuarioEmpresaService vinculoService;

    /**
     * Busca vínculos de um usuário
     * GET /api/v1/vinculo/usuario/123
     */
    @GetMapping("/usuario/{usuarioId}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<UsuarioEmpresaDTO>> buscarPorUsuario(@PathVariable Long usuarioId) {
        log.info("Buscando vínculos do usuário: {}", usuarioId);

        List<UsuarioEmpresaDTO> vinculos = vinculoService.buscarPorUsuario(usuarioId);
        return ResponseEntity.ok(vinculos);
    }

    /**
     * Busca colaboradores ativos de uma empresa
     * GET /api/v1/vinculo/empresa/123?page=0&size=20
     */
    @GetMapping("/empresa/{empresaId}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<UsuarioEmpresaDTO>> buscarPorEmpresa(
            @PathVariable Long empresaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Buscando colaboradores da empresa: {}", empresaId);

        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioEmpresaDTO> vinculos = vinculoService.buscarAtivosEmpresa(empresaId, pageable);

        return ResponseEntity.ok(vinculos);
    }

    /**
     * Cria novo vínculo usuário-empresa (admissão)
     * POST /api/v1/vinculo
     */
    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioEmpresaDTO> criar(@Valid @RequestBody CreateUsuarioEmpresaDTO dto) {
        log.info("Criando novo vínculo - Usuário: {}, Empresa: {}",
                dto.getIdUsuario(), dto.getIdEmpresa());

        UsuarioEmpresaDTO vinculo = vinculoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vinculo);
    }

    /**
     * Desativa vínculo (demissão)
     * PUT /api/v1/vinculo/123/desativar
     */
    @PutMapping("/{id}/desativar")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioEmpresaDTO> desativar(@PathVariable Long id) {
        log.info("Desativando vínculo: {}", id);

        UsuarioEmpresaDTO vinculo = vinculoService.desativar(id);
        return ResponseEntity.ok(vinculo);
    }

    /**
     * Reativa vínculo
     * PUT /api/v1/vinculo/123/reativar
     */
    @PutMapping("/{id}/reativar")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioEmpresaDTO> reativar(@PathVariable Long id) {
        log.info("Reativando vínculo: {}", id);

        UsuarioEmpresaDTO vinculo = vinculoService.reativar(id);
        return ResponseEntity.ok(vinculo);
    }

    /**
     * Atualiza cargo do usuário
     * PUT /api/v1/vinculo/123/cargo
     */
    @PutMapping("/{id}/cargo")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioEmpresaDTO> atualizarCargo(
            @PathVariable Long id,
            @RequestParam String cargo) {

        log.info("Atualizando cargo do vínculo: {}", id);

        UsuarioEmpresaDTO vinculo = vinculoService.atualizarCargo(id, cargo);
        return ResponseEntity.ok(vinculo);
    }

    /**
     * Total de colaboradores ativos
     * GET /api/v1/vinculo/empresa/123/total
     */
    @GetMapping("/empresa/{empresaId}/total")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> totalAtivos(@PathVariable Long empresaId) {
        log.info("Contando colaboradores ativos da empresa: {}", empresaId);

        long total = vinculoService.contarAtivos(empresaId);
        return ResponseEntity.ok(total);
    }
}
