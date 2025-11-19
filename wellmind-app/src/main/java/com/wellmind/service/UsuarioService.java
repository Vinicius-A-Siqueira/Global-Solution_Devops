package com.wellmind.service;

import com.wellmind.dto.usuario.*;
import com.wellmind.entity.Usuario;
import com.wellmind.exception.ResourceNotFoundException;
import com.wellmind.exception.ResourceAlreadyExistsException;
import com.wellmind.mapper.UsuarioMapper;
import com.wellmind.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service para gerenciar Usuários
 *
 * Responsabilidades:
 * - CRUD de usuários
 * - Validações de negócio
 * - Caching
 * - Auditoria
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Busca todos os usuários ativos (com cache)
     */
    @Cacheable(value = "usuarios", key = "#pageable.pageNumber")
    public Page<UsuarioDTO> listarAtivos(Pageable pageable) {
        log.info("Listando usuários ativos - página {}", pageable.getPageNumber());

        return usuarioRepository.findAllActive(pageable)
                .map(usuarioMapper::toDTO);
    }

    /**
     * Busca usuário por ID
     */
    @Cacheable(value = "usuarios", key = "#id")
    public UsuarioDTO buscarPorId(Long id) {
        log.info("Buscando usuário com ID: {}", id);

        Usuario usuario = usuarioRepository.findByIdAndActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));

        return usuarioMapper.toDTO(usuario);
    }

    /**
     * Busca usuário por email
     */
    public UsuarioDTO buscarPorEmail(String email) {
        log.info("Buscando usuário com email: {}", email);

        Usuario usuario = usuarioRepository.findByEmailAndActive(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com email: " + email));

        return usuarioMapper.toDTO(usuario);
    }

    /**
     * Busca usuários por nome (busca parcial)
     */
    public Page<UsuarioDTO> buscarPorNome(String nome, Pageable pageable) {
        log.info("Buscando usuários com nome contendo: {}", nome);

        return usuarioRepository.findByNomeContaining(nome, pageable)
                .map(usuarioMapper::toDTO);
    }

    /**
     * Cria novo usuário
     */
    @CacheEvict(value = "usuarios", allEntries = true)
    public UsuarioDTO criar(CreateUsuarioDTO dto) {
        log.info("Criando novo usuário com email: {}", dto.getEmail());

        // Validar se email já existe
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    "Email já registrado: " + dto.getEmail()
            );
        }

        // Validar idade mínima (18 anos)
        int idade = LocalDate.now().getYear() - dto.getDataNascimento().getYear();
        if (idade < 18) {
            throw new IllegalArgumentException("Usuário deve ter pelo menos 18 anos");
        }

        // Mapear DTO para Entity
        Usuario usuario = usuarioMapper.toEntity(dto);

        // Criptografar senha
        usuario.setSenhaHash(passwordEncoder.encode(dto.getSenha()));
        usuario.setStatusAtivo("S");

        // Salvar
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        log.info("Usuário criado com sucesso - ID: {}", usuarioSalvo.getIdUsuario());

        return usuarioMapper.toDTO(usuarioSalvo);
    }

    /**
     * Atualiza um usuário existente
     */
    @CacheEvict(value = "usuarios", key = "#id")
    public UsuarioDTO atualizar(Long id, UpdateUsuarioDTO dto) {
        log.info("Atualizando usuário com ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));

        // Atualizar apenas campos não nulos
        if (dto.getNome() != null && !dto.getNome().isEmpty()) {
            usuario.setNome(dto.getNome());
        }

        if (dto.getTelefone() != null && !dto.getTelefone().isEmpty()) {
            usuario.setTelefone(dto.getTelefone());
        }

        // Se nova senha foi informada, criptografar
        if (dto.getNovaSenha() != null && !dto.getNovaSenha().isEmpty()) {
            usuario.setSenhaHash(passwordEncoder.encode(dto.getNovaSenha()));
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        log.info("Usuário atualizado com sucesso - ID: {}", id);

        return usuarioMapper.toDTO(usuarioAtualizado);
    }

    /**
     * Desativa um usuário (soft delete)
     */
    @CacheEvict(value = "usuarios", key = "#id")
    public void desativar(Long id) {
        log.info("Desativando usuário com ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));

        usuario.desativar();
        usuarioRepository.save(usuario);

        log.info("Usuário desativado com sucesso - ID: {}", id);
    }

    /**
     * Reativa um usuário
     */
    @CacheEvict(value = "usuarios", key = "#id")
    public void reativar(Long id) {
        log.info("Reativando usuário com ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));

        usuario.ativar();
        usuarioRepository.save(usuario);

        log.info("Usuário reativado com sucesso - ID: {}", id);
    }

    /**
     * Busca usuários que não têm registros recentes
     */
    public List<UsuarioDTO> buscarSemRegistrosRecentes() {
        log.info("Buscando usuários sem registros recentes");

        return usuarioRepository.findUsuariosSemRegistrosRecentes()
                .stream()
                .map(usuarioMapper::toDTO)
                .toList();
    }

    /**
     * Conta usuários ativos
     */
    public long contarAtivos() {
        return usuarioRepository.countActiveUsers();
    }
}
