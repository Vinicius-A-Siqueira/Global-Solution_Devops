package com.wellmind.controller;

import com.wellmind.dto.profissionalsaude.*;
import com.wellmind.service.ProfissionalSaudeService;
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
 * Controller para Profissionais de Saúde
 */
@RestController
@RequestMapping("/api/v1/profissional")
@RequiredArgsConstructor
@Slf4j
public class ProfissionalSaudeController {

    private final ProfissionalSaudeService profissionalService;

    /**
     * Lista profissionais disponíveis
     * GET /api/v1/profissional?page=0&size=20
     */
    @GetMapping
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<ProfissionalSaudeDTO>> listarDisponíveis(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Listando profissionais disponíveis");

        Pageable pageable = PageRequest.of(page, size);
        Page<ProfissionalSaudeDTO> profissionais = profissionalService.listarDisponíveis(pageable);

        return ResponseEntity.ok(profissionais);
    }

    /**
     * Busca profissional por ID
     * GET /api/v1/profissional/123
     */
    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ProfissionalSaudeDTO> buscarPorId(@PathVariable Long id) {
        log.info("Buscando profissional por ID: {}", id);

        ProfissionalSaudeDTO profissional = profissionalService.buscarPorId(id);
        return ResponseEntity.ok(profissional);
    }

    /**
     * Busca profissionais por especialidade
     * GET /api/v1/profissional/especialidade/Psicologia?page=0&size=10
     */
    @GetMapping("/especialidade/{especialidade}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<ProfissionalSaudeDTO>> buscarPorEspecialidade(
            @PathVariable String especialidade,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Buscando profissionais de: {}", especialidade);

        Pageable pageable = PageRequest.of(page, size);
        Page<ProfissionalSaudeDTO> profissionais = profissionalService.buscarPorEspecialidade(
                especialidade, pageable
        );

        return ResponseEntity.ok(profissionais);
    }

    /**
     * Lista todas as especialidades
     * GET /api/v1/profissional/especialidades
     */
    @GetMapping("/especialidades")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<String>> listarEspecialidades() {
        log.info("Listando especialidades");

        List<String> especialidades = profissionalService.listarEspecialidades();
        return ResponseEntity.ok(especialidades);
    }

    /**
     * Cria novo profissional
     * POST /api/v1/profissional
     */
    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfissionalSaudeDTO> criar(@Valid @RequestBody CreateProfissionalSaudeDTO dto) {
        log.info("Criando novo profissional: {}", dto.getNome());

        ProfissionalSaudeDTO profissional = profissionalService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(profissional);
    }

    /**
     * Atualiza disponibilidade
     * PUT /api/v1/profissional/123/disponibilidade?disponivel=true
     */
    @PutMapping("/{id}/disponibilidade")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfissionalSaudeDTO> atualizarDisponibilidade(
            @PathVariable Long id,
            @RequestParam Boolean disponivel) {

        log.info("Atualizando disponibilidade do profissional: {}", id);

        ProfissionalSaudeDTO profissional = profissionalService.atualizarDisponibilidade(id, disponivel);
        return ResponseEntity.ok(profissional);
    }

    /**
     * Desativa profissional
     * DELETE /api/v1/profissional/123
     */
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        log.info("Desativando profissional: {}", id);

        profissionalService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Total de profissionais disponíveis
     * GET /api/v1/profissional/total/disponíveis
     */
    @GetMapping("/total/disponíveis")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> totalDisponíveis() {
        log.info("Contando profissionais disponíveis");

        long total = profissionalService.contarDisponíveis();
        return ResponseEntity.ok(total);
    }
}
