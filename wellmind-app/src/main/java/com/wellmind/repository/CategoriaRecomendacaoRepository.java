package com.wellmind.repository;

import com.wellmind.entity.CategoriaRecomendacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRecomendacaoRepository extends JpaRepository<CategoriaRecomendacao, Long> {

    /**
     * Busca categoria por nome
     */
    Optional<CategoriaRecomendacao> findByNomeCategoria(String nomeCategoria);

    /**
     * Busca categoria por nome e status ativo
     */
    @Query("SELECT c FROM CategoriaRecomendacao c WHERE c.nomeCategoria = :nome " +
            "AND c.statusAtivo = 'S'")
    Optional<CategoriaRecomendacao> findByNomeCategoriaAndActive(@Param("nome") String nome);

    /**
     * Lista todas as categorias ativas ordenadas
     */
    @Query("SELECT c FROM CategoriaRecomendacao c WHERE c.statusAtivo = 'S' " +
            "ORDER BY c.ordemExibicao, c.nomeCategoria")
    List<CategoriaRecomendacao> findAllActiveOrdered();

    /**
     * Lista categorias por status
     */
    List<CategoriaRecomendacao> findByStatusAtivo(String status);

    /**
     * Conta categorias ativas
     */
    @Query("SELECT COUNT(c) FROM CategoriaRecomendacao c WHERE c.statusAtivo = 'S'")
    long countActive();

    /**
     * Verifica se nome de categoria já existe
     */
    boolean existsByNomeCategoria(String nomeCategoria);
}
