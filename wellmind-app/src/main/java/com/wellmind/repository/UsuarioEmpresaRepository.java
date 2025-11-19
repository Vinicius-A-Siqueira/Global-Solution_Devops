package com.wellmind.repository;

import com.wellmind.entity.UsuarioEmpresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsuarioEmpresaRepository extends JpaRepository<UsuarioEmpresa, Long> {

    /**
     * Busca vínculos por usuário
     */
    @Query("SELECT ue FROM UsuarioEmpresa ue WHERE ue.usuario.idUsuario = :usuarioId " +
            "ORDER BY ue.dataVinculo DESC")
    List<UsuarioEmpresa> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca vínculos ativos por usuário
     */
    @Query("SELECT ue FROM UsuarioEmpresa ue WHERE ue.usuario.idUsuario = :usuarioId " +
            "AND ue.statusVinculo = 'A' ORDER BY ue.dataVinculo DESC")
    List<UsuarioEmpresa> findActiveByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca vínculos por empresa
     */
    @Query("SELECT ue FROM UsuarioEmpresa ue WHERE ue.empresa.idEmpresa = :empresaId " +
            "ORDER BY ue.dataVinculo DESC")
    Page<UsuarioEmpresa> findByEmpresaId(@Param("empresaId") Long empresaId, Pageable pageable);

    /**
     * Busca vínculos ativos por empresa
     */
    @Query("SELECT ue FROM UsuarioEmpresa ue WHERE ue.empresa.idEmpresa = :empresaId " +
            "AND ue.statusVinculo = 'A' ORDER BY ue.usuario.nome")
    Page<UsuarioEmpresa> findActiveByEmpresaId(@Param("empresaId") Long empresaId, Pageable pageable);

    /**
     * Busca vínculo específico usuário-empresa
     */
    @Query("SELECT ue FROM UsuarioEmpresa ue WHERE ue.usuario.idUsuario = :usuarioId " +
            "AND ue.empresa.idEmpresa = :empresaId")
    Optional<UsuarioEmpresa> findByUsuarioAndEmpresa(@Param("usuarioId") Long usuarioId,
                                                     @Param("empresaId") Long empresaId);

    /**
     * Verifica se já existe vínculo ativo
     */
    @Query("SELECT COUNT(ue) > 0 FROM UsuarioEmpresa ue " +
            "WHERE ue.usuario.idUsuario = :usuarioId " +
            "AND ue.empresa.idEmpresa = :empresaId " +
            "AND ue.statusVinculo = 'A'")
    boolean existsActiveVinculo(@Param("usuarioId") Long usuarioId,
                                @Param("empresaId") Long empresaId);

    /**
     * Conta colaboradores ativos por empresa
     */
    @Query("SELECT COUNT(ue) FROM UsuarioEmpresa ue " +
            "WHERE ue.empresa.idEmpresa = :empresaId " +
            "AND ue.statusVinculo = 'A'")
    long countActiveByEmpresaId(@Param("empresaId") Long empresaId);

    /**
     * Busca usuários por cargo
     */
    @Query("SELECT ue FROM UsuarioEmpresa ue WHERE LOWER(ue.cargo) LIKE LOWER(CONCAT('%', :cargo, '%')) " +
            "AND ue.statusVinculo = 'A'")
    Page<UsuarioEmpresa> findByCargo(@Param("cargo") String cargo, Pageable pageable);
}
