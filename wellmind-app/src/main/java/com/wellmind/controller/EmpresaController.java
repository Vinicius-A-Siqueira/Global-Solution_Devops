package com.wellmind.controller;

import com.wellmind.dto.empresa.*;
import com.wellmind.service.EmpresaService;
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

/**
 * Controller para Empresas
 *
 * Endpoints:
 * - GET /api/v1/empresa - Listar empresas
 * - GET /api/v1/empresa/{id} - Buscar por ID
 * - GET /api/v1/empresa/cnpj/{cnpj} - Buscar por CNPJ
 * - GET /api/v1/empresa/nome/{nome} - Buscar por nome
 * - POST /api/v1/empresa - Criar empresa
 * - PUT /api/v1/empresa/{id} - Atualizar empresa
 * - DELETE /api/v1/empresa/{id} - Desativar empresa
 */
@RestController
@RequestMapping("/api/v1/empresa")
@RequiredArgsConstructor
@Slf4j
public class EmpresaController {

    private final EmpresaService empresaService;

    /**
     * Lista empresas ativas
     * GET /api/v1/empresa?page=0&size=20
     */
    @GetMapping
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<EmpresaDTO>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Listando empresas - página: {}, tamanho: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EmpresaDTO> empresas = empresaService.listarAtivas(pageable);

        return ResponseEntity.ok(empresas);
    }

    /**
     * Busca empresa por ID
     * GET /api/v1/empresa/123
     */
    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<EmpresaDTO> buscarPorId(@PathVariable Long id) {
        log.info("Buscando empresa por ID: {}", id);

        EmpresaDTO empresa = empresaService.buscarPorId(id);
        return ResponseEntity.ok(empresa);
    }

    /**
     * Busca empresa por CNPJ
     * GET /api/v1/empresa/cnpj/12345678901234
     */
    @GetMapping("/cnpj/{cnpj}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<EmpresaDTO> buscarPorCnpj(@PathVariable String cnpj) {
        log.info("Buscando empresa por CNPJ: {}", cnpj);

        EmpresaDTO empresa = empresaService.buscarPorCnpj(cnpj);
        return ResponseEntity.ok(empresa);
    }

    /**
     * Busca empresas por nome
     * GET /api/v1/empresa/nome/TechCorp?page=0&size=10
     */
    @GetMapping("/nome/{nome}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<EmpresaDTO>> buscarPorNome(
            @PathVariable String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Buscando empresas com nome: {}", nome);

        Pageable pageable = PageRequest.of(page, size);
        Page<EmpresaDTO> empresas = empresaService.buscarPorNome(nome, pageable);

        return ResponseEntity.ok(empresas);
    }

    /**
     * Cria nova empresa
     * POST /api/v1/empresa
     */
    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmpresaDTO> criar(@Valid @RequestBody CreateEmpresaDTO dto) {
        log.info("Criando nova empresa: {}", dto.getNomeEmpresa());

        EmpresaDTO empresa = empresaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(empresa);
    }

    /**
     * Atualiza empresa
     * PUT /api/v1/empresa/123
     */
    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmpresaDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmpresaDTO dto) {

        log.info("Atualizando empresa: {}", id);

        EmpresaDTO empresa = empresaService.atualizar(id, dto);
        return ResponseEntity.ok(empresa);
    }

    /**
     * Desativa empresa
     * DELETE /api/v1/empresa/123
     */
    @DeleteMapping("/{id}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        log.info("Desativando empresa: {}", id);

        empresaService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca empresas com mais colaboradores
     * GET /api/v1/empresa/ranking/colaboradores?page=0&size=10
     */
    @GetMapping("/ranking/colaboradores")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<EmpresaDTO>> buscarComMaisColaboradores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Buscando empresas com mais colaboradores");

        Pageable pageable = PageRequest.of(page, size);
        Page<EmpresaDTO> empresas = empresaService.buscarComMaisColaboradores(pageable);

        return ResponseEntity.ok(empresas);
    }

    /**
     * Total de empresas ativas
     * GET /api/v1/empresa/total/ativas
     */
    @GetMapping("/total/ativas")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> totalAtivas() {
        log.info("Contando empresas ativas");

        long total = empresaService.contarAtivas();
        return ResponseEntity.ok(total);
    }
}
