package com.wellmind.service;

import com.wellmind.dto.auth.*;
import com.wellmind.dto.usuario.CreateUsuarioDTO;
import com.wellmind.entity.Usuario;
import com.wellmind.exception.ResourceAlreadyExistsException;
import com.wellmind.exception.ResourceNotFoundException;
import com.wellmind.mapper.UsuarioMapper;
import com.wellmind.repository.UsuarioRepository;
import com.wellmind.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Service para Autenticação e Autorização
 *
 * Responsabilidades:
 * - Login de usuários
 * - Registro de novos usuários
 * - Refresh de tokens JWT
 * - Validações de autenticação
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Realiza login do usuário
     *
     * @param dto LoginDTO com email e senha
     * @return AuthResponseDTO com token JWT
     * @throws ResourceNotFoundException se usuário não encontrado
     */
    public AuthResponseDTO login(LoginDTO dto) {
        log.info("Tentativa de login - email: {}", dto.getEmail());

        // Buscar usuário no banco
        Usuario usuario = usuarioRepository.findByEmailAndActive(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com email: " + dto.getEmail()
                ));

        // Verificar senha
        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenhaHash())) {
            log.warn("Tentativa de login com senha incorreta - email: {}", dto.getEmail());
            throw new IllegalArgumentException("Email ou senha inválidos");
        }

        // Autenticar
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getEmail(),
                            dto.getSenha()
                    )
            );

            // Gerar token JWT
            String token = jwtTokenProvider.generateToken(authentication);

            log.info("Login bem-sucedido - email: {}", dto.getEmail());

            return AuthResponseDTO.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .expiresIn(86400L) // 24 horas em segundos
                    .usuario(usuario.getNome())
                    .email(usuario.getEmail())
                    .mensagem("Login realizado com sucesso")
                    .build();

        } catch (Exception e) {
            log.error("Erro ao autenticar usuário: {}", e.getMessage());
            throw new IllegalArgumentException("Falha na autenticação");
        }
    }

    /**
     * Registra novo usuário
     *
     * @param dto RegisterDTO com dados do novo usuário
     * @return AuthResponseDTO com token JWT
     * @throws ResourceAlreadyExistsException se email já existe
     */
    public AuthResponseDTO register(RegisterDTO dto) {
        log.info("Tentativa de registro - email: {}", dto.getEmail());

        // Validar se email já existe
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            log.warn("Tentativa de registro com email já existente: {}", dto.getEmail());
            throw new ResourceAlreadyExistsException(
                    "Email já registrado: " + dto.getEmail()
            );
        }

        // Validar idade mínima (18 anos)
        int idade = LocalDate.now().getYear() - dto.getDataNascimento().getYear();
        if (idade < 18) {
            throw new IllegalArgumentException("Usuário deve ter pelo menos 18 anos");
        }

        // Validar força da senha (já feito em DTO com @Pattern, mas fazer dupla validação)
        validarSenhaForte(dto.getSenha());

        // Converter DTO para Entity
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senhaHash(passwordEncoder.encode(dto.getSenha()))
                .dataNascimento(dto.getDataNascimento())
                .genero(dto.getGenero())
                .telefone(dto.getTelefone())
                .statusAtivo("S")
                .dataCadastro(LocalDateTime.now())
                .build();

        // Salvar novo usuário
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        log.info("Usuário registrado com sucesso - ID: {}, email: {}",
                usuarioSalvo.getIdUsuario(), usuarioSalvo.getEmail());

        // Autenticar e retornar token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getSenha()
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .usuario(usuarioSalvo.getNome())
                .email(usuarioSalvo.getEmail())
                .mensagem("Usuário registrado com sucesso")
                .build();
    }

    /**
     * Refresh do token JWT
     *
     * @param token Token anterior
     * @return AuthResponseDTO com novo token
     */
    public AuthResponseDTO refreshToken(String token) {
        log.info("Tentativa de refresh de token");

        // Remover "Bearer " do token
        String jwtToken = token.replace("Bearer ", "");

        // Validar token
        if (!jwtTokenProvider.validateToken(jwtToken)) {
            log.warn("Token inválido ao tentar refresh");
            throw new IllegalArgumentException("Token inválido");
        }

        // Extrair username do token
        String username = jwtTokenProvider.getUsernameFromToken(jwtToken);

        // Buscar usuário
        Usuario usuario = usuarioRepository.findByEmailAndActive(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // Criar nova autenticação
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username, null, usuario.getAuthorities()
        );

        // Gerar novo token
        String novoToken = jwtTokenProvider.generateToken(authentication);

        log.info("Token refreshado com sucesso para usuário: {}", username);

        return AuthResponseDTO.builder()
                .token(novoToken)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .usuario(usuario.getNome())
                .email(usuario.getEmail())
                .mensagem("Token renovado com sucesso")
                .build();
    }

    /**
     * Valida força da senha
     *
     * @param senha Senha a validar
     * @throws IllegalArgumentException se senha fraca
     */
    private void validarSenhaForte(String senha) {
        if (senha == null || senha.length() < 8) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 8 caracteres");
        }

        if (!senha.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Senha deve conter pelo menos uma letra maiúscula");
        }

        if (!senha.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Senha deve conter pelo menos um número");
        }

        if (!senha.matches(".*[!@#$%^&*].*")) {
            throw new IllegalArgumentException("Senha deve conter pelo menos um caractere especial (!@#$%^&*)");
        }
    }

    /**
     * Busca usuário autenticado
     *
     * @param email Email do usuário
     * @return Usuario encontrado
     */
    public Usuario buscarUsuarioAutenticado(String email) {
        return usuarioRepository.findByEmailAndActive(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    /**
     * Verifica se email está registrado
     */
    public boolean emailRegistrado(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
