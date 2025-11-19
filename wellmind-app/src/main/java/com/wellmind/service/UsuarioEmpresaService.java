package com.wellmind.service;

import com.wellmind.dto.usuarioempresa.*;
import com.wellmind.entity.Usuario;
import com.wellmind.entity.Empresa;
import com.wellmind.entity.UsuarioEmpresa;
import com.wellmind.exception.ResourceNotFoundException;
import com.wellmind.exception.ResourceAlreadyExistsException;
import com.wellmind.mapper.UsuarioEmpresaMapper;
import com.wellmind.repository.UsuarioRepository;
import com.wellmind.repository.EmpresaRepository;
import com.wellmind.repository.UsuarioEmpresaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service para gerenciar vínculos Usuário-Empresa
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioEmpresaService {

    private final UsuarioEmpresaRepository usuarioEmpresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioEmpresaMapper usuarioEmpresaMapper;

    /**
     * Busca vínculos de um usuário
     */
    public List<UsuarioEmpresaDTO> buscarPorUsuario(Long usuarioId) {
        log.info("Buscando vínculos do usuário: {}", usuarioId);

        // Validar se usuário existe
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return usuarioEmpresaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(usuarioEmpresaMapper::toDTO)
                .toList();
    }

    /**
     * Busca vínculos ativos de uma empresa
     */
    @Cacheable(value = "vinculosempresa", key = "#empresaId + '_' + #pageable.pageNumber")
    public Page<UsuarioEmpresaDTO> buscarAtivosEmpresa(Long empresaId, Pageable pageable) {
        log.info("Buscando vínculos ativos da empresa: {}", empresaId);

        // Validar se empresa existe
        empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada"));

        return usuarioEmpresaRepository.findActiveByEmpresaId(empresaId, pageable)
                .map(usuarioEmpresaMapper::toDTO);
    }

    /**
     * Cria novo vínculo usuário-empresa
     */
    @CacheEvict(value = {"vinculosempresa", "usuarios", "empresas"}, allEntries = true)
    public UsuarioEmpresaDTO criar(CreateUsuarioEmpresaDTO dto) {
        log.info("Criando vínculo - Usuário: {}, Empresa: {}", dto.getIdUsuario(), dto.getIdEmpresa());

        // Buscar usuário e empresa
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Empresa empresa = empresaRepository.findById(dto.getIdEmpresa())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada"));

        // Verificar se já existe vínculo ativo
        if (usuarioEmpresaRepository.existsActiveVinculo(dto.getIdUsuario(), dto.getIdEmpresa())) {
            throw new ResourceAlreadyExistsException(
                    "Usuário já possui vínculo ativo com esta empresa"
            );
        }

        // Criar vínculo
        UsuarioEmpresa vinculo = usuarioEmpresaMapper.toEntity(dto);
        vinculo.setUsuario(usuario);
        vinculo.setEmpresa(empresa);
        vinculo.setStatusVinculo("A");

        UsuarioEmpresa vinculoSalvo = usuarioEmpresaRepository.save(vinculo);
        log.info("Vínculo criado com sucesso - ID: {}", vinculoSalvo.getIdUsuarioEmpresa());

        return usuarioEmpresaMapper.toDTO(vinculoSalvo);
    }

    /**
     * Desativa um vínculo (demissão)
     */
    @CacheEvict(value = "vinculosempresa", allEntries = true)
    public UsuarioEmpresaDTO desativar(Long vinculoId) {
        log.info("Desativando vínculo com ID: {}", vinculoId);

        UsuarioEmpresa vinculo = usuarioEmpresaRepository.findById(vinculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Vínculo não encontrado"));

        vinculo.desativarVinculo();

        UsuarioEmpresa vinculoAtualizado = usuarioEmpresaRepository.save(vinculo);
        log.info("Vínculo desativado com sucesso");

        return usuarioEmpresaMapper.toDTO(vinculoAtualizado);
    }

    /**
     * Reativa um vínculo
     */
    @CacheEvict(value = "vinculosempresa", allEntries = true)
    public UsuarioEmpresaDTO reativar(Long vinculoId) {
        log.info("Reativando vínculo com ID: {}", vinculoId);

        UsuarioEmpresa vinculo = usuarioEmpresaRepository.findById(vinculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Vínculo não encontrado"));

        vinculo.ativarVinculo();

        UsuarioEmpresa vinculoAtualizado = usuarioEmpresaRepository.save(vinculo);
        log.info("Vínculo reativado com sucesso");

        return usuarioEmpresaMapper.toDTO(vinculoAtualizado);
    }

    /**
     * Atualiza cargo do usuário
     */
    public UsuarioEmpresaDTO atualizarCargo(Long vinculoId, String novoCargo) {
        log.info("Atualizando cargo do vínculo: {}", vinculoId);

        UsuarioEmpresa vinculo = usuarioEmpresaRepository.findById(vinculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Vínculo não encontrado"));

        vinculo.setCargo(novoCargo);

        UsuarioEmpresa vinculoAtualizado = usuarioEmpresaRepository.save(vinculo);
        log.info("Cargo atualizado com sucesso");

        return usuarioEmpresaMapper.toDTO(vinculoAtualizado);
    }

    /**
     * Conta colaboradores ativos de uma empresa
     */
    public long contarAtivos(Long empresaId) {
        return usuarioEmpresaRepository.countActiveByEmpresaId(empresaId);
    }
}
