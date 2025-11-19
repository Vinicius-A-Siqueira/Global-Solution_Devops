package com.wellmind.repository;

import com.wellmind.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca usuário por email (case-insensitive)
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<Usuario> findByEmail(@Param("email") String email);

    /**
     * Busca usuário por email e status ativo
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.email) = LOWER(:email) AND u.statusAtivo = 'S'")
    Optional<Usuario> findByEmailAndActive(@Param("email") String email);

    /**
     * Busca usuário por ID e status ativo
     */
    @Query("SELECT u FROM Usuario u WHERE u.idUsuario = :id AND u.statusAtivo = 'S'")
    Optional<Usuario> findByIdAndActive(@Param("id") Long id);

    /**
     * Lista todos os usuários ativos (paginado)
     */
    @Query("SELECT u FROM Usuario u WHERE u.statusAtivo = 'S' ORDER BY u.nome")
    Page<Usuario> findAllActive(Pageable pageable);

    /**
     * Lista usuários por status
     */
    List<Usuario> findByStatusAtivo(String status);

    /**
     * Busca usuários por nome (case-insensitive, parcial)
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%')) " +
            "AND u.statusAtivo = 'S'")
    Page<Usuario> findByNomeContaining(@Param("nome") String nome, Pageable pageable);

    /**
     * Busca usuários por faixa etária
     */
    @Query("SELECT u FROM Usuario u WHERE u.dataNascimento BETWEEN :dataInicio AND :dataFim " +
            "AND u.statusAtivo = 'S'")
    List<Usuario> findByFaixaEtaria(@Param("dataInicio") LocalDate dataInicio,
                                    @Param("dataFim") LocalDate dataFim);

    /**
     * Conta total de usuários ativos
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.statusAtivo = 'S'")
    long countActiveUsers();

    /**
     * Verifica se email já existe
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuários por gênero
     */
    @Query("SELECT u FROM Usuario u WHERE u.genero = :genero AND u.statusAtivo = 'S'")
    Page<Usuario> findByGenero(@Param("genero") String genero, Pageable pageable);

    /**
     * Busca usuários que precisam de atenção (sem registros recentes)
     */
    @Query("SELECT u FROM Usuario u WHERE u.statusAtivo = 'S' " +
            "AND u.idUsuario NOT IN (" +
            "  SELECT DISTINCT rb.usuario.idUsuario FROM RegistroBemestar rb " +
            "  WHERE rb.dataRegistro >= CURRENT_DATE - 7" +
            ")")
    List<Usuario> findUsuariosSemRegistrosRecentes();
}
