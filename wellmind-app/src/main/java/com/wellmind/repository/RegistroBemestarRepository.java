package com.wellmind.repository;

import com.wellmind.entity.RegistroBemestar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroBemestarRepository extends JpaRepository<RegistroBemestar, Long> {

    /**
     * Busca registros por usuário (paginado)
     */
    @Query("SELECT rb FROM RegistroBemestar rb WHERE rb.usuario.idUsuario = :usuarioId " +
            "ORDER BY rb.dataRegistro DESC")
    Page<RegistroBemestar> findByUsuarioId(@Param("usuarioId") Long usuarioId, Pageable pageable);

    /**
     * Busca registros recentes por usuário
     */
    @Query("SELECT rb FROM RegistroBemestar rb WHERE rb.usuario.idUsuario = :usuarioId " +
            "AND rb.dataRegistro >= :dataInicio ORDER BY rb.dataRegistro DESC")
    List<RegistroBemestar> findRecentByUsuarioId(@Param("usuarioId") Long usuarioId,
                                                 @Param("dataInicio") LocalDateTime dataInicio);

    /**
     * Busca último registro de um usuário
     */
    @Query("SELECT rb FROM RegistroBemestar rb WHERE rb.usuario.idUsuario = :usuarioId " +
            "ORDER BY rb.dataRegistro DESC LIMIT 1")
    Optional<RegistroBemestar> findLastByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca registros por período
     */
    @Query("SELECT rb FROM RegistroBemestar rb WHERE rb.usuario.idUsuario = :usuarioId " +
            "AND rb.dataRegistro BETWEEN :inicio AND :fim ORDER BY rb.dataRegistro DESC")
    List<RegistroBemestar> findByPeriodo(@Param("usuarioId") Long usuarioId,
                                         @Param("inicio") LocalDateTime inicio,
                                         @Param("fim") LocalDateTime fim);

    /**
     * Busca registros com estresse alto
     */
    @Query("SELECT rb FROM RegistroBemestar rb WHERE rb.nivelEstresse >= 8 " +
            "ORDER BY rb.dataRegistro DESC")
    Page<RegistroBemestar> findWithHighStress(Pageable pageable);

    /**
     * Busca registros com humor baixo
     */
    @Query("SELECT rb FROM RegistroBemestar rb WHERE rb.nivelHumor <= 3 " +
            "ORDER BY rb.dataRegistro DESC")
    Page<RegistroBemestar> findWithLowMood(Pageable pageable);

    /**
     * Calcula média de bem-estar de um usuário
     */
    @Query("SELECT AVG((rb.nivelHumor + (11 - rb.nivelEstresse) + rb.nivelEnergia) / 3.0) " +
            "FROM RegistroBemestar rb WHERE rb.usuario.idUsuario = :usuarioId " +
            "AND rb.dataRegistro >= :dataInicio")
    Double calculateAverageWellness(@Param("usuarioId") Long usuarioId,
                                    @Param("dataInicio") LocalDateTime dataInicio);

    /**
     * Conta registros por usuário no período
     */
    @Query("SELECT COUNT(rb) FROM RegistroBemestar rb WHERE rb.usuario.idUsuario = :usuarioId " +
            "AND rb.dataRegistro >= :dataInicio")
    long countByUsuarioIdAndPeriod(@Param("usuarioId") Long usuarioId,
                                   @Param("dataInicio") LocalDateTime dataInicio);

    /**
     * Busca registros que precisam de alerta
     */
    @Query("SELECT rb FROM RegistroBemestar rb WHERE " +
            "(rb.nivelEstresse >= 8 OR rb.nivelHumor <= 3 OR rb.horasSono < 6 OR rb.horasSono > 9) " +
            "AND rb.dataRegistro >= CURRENT_DATE - 1 ORDER BY rb.dataRegistro DESC")
    List<RegistroBemestar> findRequiringAlert();

    /**
     * Estatísticas de bem-estar por empresa
     */
    @Query("SELECT AVG(rb.nivelHumor), AVG(rb.nivelEstresse), AVG(rb.nivelEnergia) " +
            "FROM RegistroBemestar rb " +
            "JOIN rb.usuario u " +
            "JOIN u.empresas ue " +
            "WHERE ue.empresa.idEmpresa = :empresaId " +
            "AND rb.dataRegistro >= :dataInicio")
    List<Object[]> findStatisticsByEmpresa(@Param("empresaId") Long empresaId,
                                           @Param("dataInicio") LocalDateTime dataInicio);
}
