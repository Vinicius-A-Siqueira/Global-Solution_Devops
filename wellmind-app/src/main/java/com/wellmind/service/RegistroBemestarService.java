package com.wellmind.service;

import com.wellmind.dto.registrobemestar.*;
import com.wellmind.entity.Usuario;
import com.wellmind.entity.RegistroBemestar;
import com.wellmind.exception.ResourceNotFoundException;
import com.wellmind.mapper.RegistroBemestarMapper;
import com.wellmind.messaging.NotificationProducer;
import com.wellmind.repository.UsuarioRepository;
import com.wellmind.repository.RegistroBemestarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Service para gerenciar Registros de Bem-estar
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RegistroBemestarService {

    private final RegistroBemestarRepository registroBemestarRepository;
    private final UsuarioRepository usuarioRepository;
    private final RegistroBemestarMapper registroBemestarMapper;
    private final NotificationProducer notificationProducer;

    /**
     * Busca registros de bem-estar de um usuário
     */
    @Cacheable(value = "registros-bemestar", key = "#usuarioId + '_' + #pageable.pageNumber")
    public Page<RegistroBemestarDTO> buscarPorUsuario(Long usuarioId, Pageable pageable) {
        log.info("Buscando registros de bem-estar do usuário: {}", usuarioId);

        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return registroBemestarRepository.findByUsuarioId(usuarioId, pageable)
                .map(registroBemestarMapper::toDTO);
    }

    /**
     * Busca último registro de um usuário
     */
    public RegistroBemestarDTO buscarUltimo(Long usuarioId) {
        log.info("Buscando último registro do usuário: {}", usuarioId);

        RegistroBemestar registro = registroBemestarRepository.findLastByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Nenhum registro de bem-estar encontrado para este usuário"
                ));

        return registroBemestarMapper.toDTO(registro);
    }

    /**
     * Busca registros de um período
     */
    public List<RegistroBemestarDTO> buscarPorPeriodo(Long usuarioId,
                                                      LocalDateTime inicio,
                                                      LocalDateTime fim) {
        log.info("Buscando registros do usuário {} no período {}-{}", usuarioId, inicio, fim);

        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return registroBemestarRepository.findByPeriodo(usuarioId, inicio, fim)
                .stream()
                .map(registroBemestarMapper::toDTO)
                .toList();
    }

    /**
     * Busca registros com estresse alto
     */
    public Page<RegistroBemestarDTO> buscarComEstresseAlto(Pageable pageable) {
        log.info("Buscando registros com estresse alto");

        return registroBemestarRepository.findWithHighStress(pageable)
                .map(registroBemestarMapper::toDTO);
    }

    /**
     * Cria novo registro de bem-estar
     */
    @CacheEvict(value = "registros-bemestar", allEntries = true)
    public RegistroBemestarDTO criar(CreateRegistroBemestarDTO dto) {
        log.info("Criando novo registro de bem-estar para usuário: {}", dto.getIdUsuario());

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // Mapear DTO para Entity
        RegistroBemestar registro = registroBemestarMapper.toEntity(dto);
        registro.setUsuario(usuario);

        // Salvar registro
        RegistroBemestar registroSalvo = registroBemestarRepository.save(registro);
        log.info("Registro de bem-estar criado - ID: {}", registroSalvo.getIdRegistro());

        // Verificar se precisa de alertas
        if (registroSalvo.precisaAlerta()) {
            gerarAlertas(usuario, registroSalvo);
        }

        return registroBemestarMapper.toDTO(registroSalvo);
    }

    /**
     * Gera alertas se necessário
     */
    private void gerarAlertas(Usuario usuario, RegistroBemestar registro) {
        List<String> alertas = new ArrayList<>();

        if (registro.isEstresseAlto()) {
            alertas.add("⚠️ Nível de estresse alto");
        }

        if (registro.isHumorBaixo()) {
            alertas.add("⚠️ Humor baixo detectado");
        }

        if (registro.isSonoInadequado()) {
            alertas.add("⚠️ Padrão de sono inadequado");
        }

        if (!alertas.isEmpty()) {
            log.warn("Alertas gerados para usuário {}: {}", usuario.getIdUsuario(), alertas);

            // Enviar notificação via RabbitMQ
            notificationProducer.enviarAlerta(usuario.getEmail(), alertas);
        }
    }

    /**
     * Calcula média de bem-estar de um usuário nos últimos 30 dias
     */
    public Double calcularMediaBemEstar(Long usuarioId) {
        log.info("Calculando média de bem-estar do usuário: {}", usuarioId);

        LocalDateTime trinta_dias_atras = LocalDateTime.now().minus(30, ChronoUnit.DAYS);

        return registroBemestarRepository.calculateAverageWellness(usuarioId, trinta_dias_atras);
    }

    /**
     * Busca registros que precisam de alerta
     */
    public List<RegistroBemestarDTO> buscarComAlerta() {
        log.info("Buscando registros que precisam de alerta");

        return registroBemestarRepository.findRequiringAlert()
                .stream()
                .map(registroBemestarMapper::toDTO)
                .toList();
    }

    /**
     * Estatísticas de bem-estar por empresa
     */
    public void gerarEstatísticas(Long empresaId) {
        log.info("Gerando estatísticas de bem-estar para empresa: {}", empresaId);

        LocalDateTime trinta_dias_atras = LocalDateTime.now().minus(30, ChronoUnit.DAYS);

        List<Object[]> stats = registroBemestarRepository.findStatisticsByEmpresa(
                empresaId, trinta_dias_atras
        );

        if (!stats.isEmpty()) {
            Object[] data = stats.get(0);

            Double mediaHumor = (data[0] != null) ? ((Number)data[0]).doubleValue() : null;
            Double mediaEstresse = (data[1] != null) ? ((Number)data[1]).doubleValue() : null;
            Double mediaEnergia = (data[2] != null) ? ((Number)data[2]).doubleValue() : null;

            log.info("Estatísticas - Humor: {}, Estresse: {}, Energia: {}",
                    mediaHumor, mediaEstresse, mediaEnergia);
        }
    }
}
