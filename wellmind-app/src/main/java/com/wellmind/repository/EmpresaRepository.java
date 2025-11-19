package com.wellmind.repository;

import com.wellmind.entity.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    /**
     * Busca empresa por CNPJ
     */
    Optional<Empresa> findByCnpj(String cnpj);

    /**
     * Busca empresa por CNPJ e status ativo
     */
    @Query("SELECT e FROM Empresa e WHERE e.cnpj = :cnpj AND e.statusAtivo = 'S'")
    Optional<Empresa> findByCnpjAndActive(@Param("cnpj") String cnpj);

    /**
     * Busca empresa por ID e status ativo
     */
    @Query("SELECT e FROM Empresa e WHERE e.idEmpresa = :id AND e.statusAtivo = 'S'")
    Optional<Empresa> findByIdAndActive(@Param("id") Long id);

    /**
     * Lista todas as empresas ativas (paginado)
     */
    @Query("SELECT e FROM Empresa e WHERE e.statusAtivo = 'S' ORDER BY e.nomeEmpresa")
    Page<Empresa> findAllActive(Pageable pageable);

    /**
     * Lista empresas por status
     */
    List<Empresa> findByStatusAtivo(String status);

    /**
     * Busca empresas por nome (case-insensitive, parcial)
     */
    @Query("SELECT e FROM Empresa e WHERE LOWER(e.nomeEmpresa) LIKE LOWER(CONCAT('%', :nome, '%')) " +
            "AND e.statusAtivo = 'S'")
    Page<Empresa> findByNomeContaining(@Param("nome") String nome, Pageable pageable);

    /**
     * Conta total de empresas ativas
     */
    @Query("SELECT COUNT(e) FROM Empresa e WHERE e.statusAtivo = 'S'")
    long countActiveEmpresas();

    /**
     * Verifica se CNPJ já existe
     */
    boolean existsByCnpj(String cnpj);

    /**
     * Busca empresas com mais colaboradores
     */
    @Query("SELECT e FROM Empresa e " +
            "LEFT JOIN e.usuarios ue " +
            "WHERE e.statusAtivo = 'S' " +
            "GROUP BY e " +
            "ORDER BY COUNT(ue) DESC")
    Page<Empresa> findEmpresasComMaisColaboradores(Pageable pageable);

    /**
     * Busca empresas por cidade (extraído do endereço)
     */
    @Query("SELECT e FROM Empresa e WHERE LOWER(e.endereco) LIKE LOWER(CONCAT('%', :cidade, '%')) " +
            "AND e.statusAtivo = 'S'")
    List<Empresa> findByCidade(@Param("cidade") String cidade);
}
