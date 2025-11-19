package com.wellmind.controller;

import com.wellmind.dto.registrobemestar.*;
import com.wellmind.service.RegistroBemestarService;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller para Registros de Bem-estar
 *
 * Endpoints:
 * - GET /api/v1/wellness - Listar registros
 * - GET /api/v1/wellness/usuario/{id} - Registros do usuário
 * - GET /api/v1/wellness/usuario/{id}/ultimo - Último registro
 * - POST /api/v1/wellness - Criar registro
 * - GET /api/v1/wellness/alertas - Registros com alerta
 * - GET /api/v1/wellness/estatísticas/{empresaId} - Estatísticas
 */
@RestController
@RequestMapping("/api/v1/wellness")
@RequiredArgsConstructor
@Slf4j
public class RegistroBemestarController {

    private final RegistroBemestarService bemestarService;

    /**
     * Busca registros de bem-estar de um usuário
     * GET /api/v1/wellness/usuario/123?page=0&size=30
     */
    @GetMapping("/usuario/{usuarioId}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<RegistroBemestarDTO>> buscarPorUsuario(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {

        log.info("Buscando registros de bem-estar do usuário: {}", usuarioId);

        Pageable pageable = PageRequest.of(page, size);
        Page<RegistroBemestarDTO> registros = bemestarService.buscarPorUsuario(usuarioId, pageable);

        return ResponseEntity.ok(registros);
    }

    /**
     * Busca último registro de bem-estar
     * GET /api/v1/wellness/usuario/123/ultimo
     */
    @GetMapping("/usuario/{usuarioId}/ultimo")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<RegistroBemestarDTO> buscarUltimo(@PathVariable Long usuarioId) {
        log.info("Buscando último registro do usuário: {}", usuarioId);

        RegistroBemestarDTO registro = bemestarService.buscarUltimo(usuarioId);
        return ResponseEntity.ok(registro);
    }

    /**
     * Busca registros com estresse alto
     * GET /api/v1/wellness/estresse-alto?page=0&size=20
     */
    @GetMapping("/estresse-alto")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<RegistroBemestarDTO>> buscarComEstresseAlto(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Buscando registros com estresse alto");

        Pageable pageable = PageRequest.of(page, size);
        Page<RegistroBemestarDTO> registros = bemestarService.buscarComEstresseAlto(pageable);

        return ResponseEntity.ok(registros);
    }

    /**
     * Cria novo registro de bem-estar
     * POST /api/v1/wellness
     */
    @PostMapping
   // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<RegistroBemestarDTO> criar(@Valid @RequestBody CreateRegistroBemestarDTO dto) {
        log.info("Criando novo registro de bem-estar para usuário: {}", dto.getIdUsuario());

        RegistroBemestarDTO registro = bemestarService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registro);
    }

    /**
     * Busca registros que precisam de alerta
     * GET /api/v1/wellness/alertas
     */
    @GetMapping("/alertas")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RegistroBemestarDTO>> buscarComAlerta() {
        log.info("Buscando registros com alerta");

        List<RegistroBemestarDTO> registros = bemestarService.buscarComAlerta();
        return ResponseEntity.ok(registros);
    }

    /**
     * Calcula média de bem-estar do usuário (últimos 30 dias)
     * GET /api/v1/wellness/usuario/123/media
     */
    @GetMapping("/usuario/{usuarioId}/media")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> calcularMedia(@PathVariable Long usuarioId) {
        log.info("Calculando média de bem-estar: {}", usuarioId);

        Double media = bemestarService.calcularMediaBemEstar(usuarioId);

        Map<String, Object> response = new HashMap<>();
        response.put("usuarioId", usuarioId);
        response.put("mediaBemestar", media);

        if (media != null) {
            if (media >= 8) response.put("classificacao", "EXCELENTE");
            else if (media >= 6) response.put("classificacao", "BOM");
            else if (media >= 4) response.put("classificacao", "REGULAR");
            else if (media >= 2) response.put("classificacao", "RUIM");
            else response.put("classificacao", "CRÍTICO");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Gera estatísticas de bem-estar por empresa
     * GET /api/v1/wellness/estatísticas/123
     */
    @GetMapping("/estatísticas/{empresaId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> gerarEstatísticas(@PathVariable Long empresaId) {
        log.info("Gerando estatísticas de bem-estar para empresa: {}", empresaId);

        bemestarService.gerarEstatísticas(empresaId);
        return ResponseEntity.noContent().build();
    }
}