package com.wellmind.repository;

import com.wellmind.entity.ProfissionalSaude;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaude, Long> {

    /**
     * Busca profissional por CRP/CRM
     */
    Optional<ProfissionalSaude> findByCrpCrm(String crpCrm);

    /**
     * Busca profissional por CRP/CRM e status ativo
     */
    @Query("SELECT p FROM ProfissionalSaude p WHERE p.crpCrm = :crpCrm " +
            "AND p.statusAtivo = 'S'")
    Optional<ProfissionalSaude> findByCrpCrmAndActive(@Param("crpCrm") String crpCrm);

    /**
     * Lista profissionais disponíveis (paginado)
     */
    @Query("SELECT p FROM ProfissionalSaude p WHERE p.disponivel = 'S' " +
            "AND p.statusAtivo = 'S' ORDER BY p.nome")
    Page<ProfissionalSaude> findAllAvailable(Pageable pageable);

    /**
     * Lista profissionais por especialidade
     */
    @Query("SELECT p FROM ProfissionalSaude p WHERE LOWER(p.especialidade) LIKE LOWER(CONCAT('%', :especialidade, '%')) " +
            "AND p.statusAtivo = 'S' AND p.disponivel = 'S' ORDER BY p.nome")
    Page<ProfissionalSaude> findByEspecialidade(@Param("especialidade") String especialidade,
                                                Pageable pageable);

    /**
     * Busca profissionais por nome
     */
    @Query("SELECT p FROM ProfissionalSaude p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')) " +
            "AND p.statusAtivo = 'S' ORDER BY p.nome")
    Page<ProfissionalSaude> findByNomeContaining(@Param("nome") String nome, Pageable pageable);

    /**
     * Lista todas as especialidades únicas
     */
    @Query("SELECT DISTINCT p.especialidade FROM ProfissionalSaude p " +
            "WHERE p.statusAtivo = 'S' ORDER BY p.especialidade")
    List<String> findAllEspecialidades();

    /**
     * Conta profissionais disponíveis
     */
    @Query("SELECT COUNT(p) FROM ProfissionalSaude p WHERE p.disponivel = 'S' " +
            "AND p.statusAtivo = 'S'")
    long countAvailable();

    /**
     * Verifica se CRP/CRM já existe
     */
    boolean existsByCrpCrm(String crpCrm);

    /**
     * Busca profissionais por faixa de preço
     */
    @Query("SELECT p FROM ProfissionalSaude p WHERE p.valorConsulta BETWEEN :min AND :max " +
            "AND p.statusAtivo = 'S' AND p.disponivel = 'S' ORDER BY p.valorConsulta")
    List<ProfissionalSaude> findByFaixaPreco(@Param("min") Double min, @Param("max") Double max);
}
