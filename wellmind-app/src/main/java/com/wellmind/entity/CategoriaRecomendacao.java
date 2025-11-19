package com.wellmind.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "CATEGORIA_RECOMENDACAO", indexes = {
        @Index(name = "idx_cat_nome", columnList = "NOME_CATEGORIA")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "idCategoria")
public class CategoriaRecomendacao {

    @Id
    @SequenceGenerator(
            name = "seq_categoria_recomendacao_gen",
            sequenceName = "seq_categoria_recomendacao",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_categoria_recomendacao_gen"
    )
    @Column(name = "ID_CATEGORIA")
    private Long idCategoria;

    @Column(name = "NOME_CATEGORIA", nullable = false, unique = true, length = 100)
    private String nomeCategoria;

    @Column(name = "DESCRICAO", length = 500)
    private String descricao;

    @Column(name = "ICONE", length = 50)
    private String icone;

    @Column(name = "ORDEM_EXIBICAO")
    private Integer ordemExibicao;

    @Column(name = "STATUS_ATIVO", length = 1)
    private String statusAtivo;

    // Lifecycle Callbacks

    @PrePersist
    protected void onCreate() {
        if (statusAtivo == null) {
            statusAtivo = "S";
        }
    }

    // Business Methods

    /**
     * Verifica se a categoria está ativa
     */
    public boolean isAtiva() {
        return "S".equals(this.statusAtivo);
    }

    /**
     * Ativa a categoria
     */
    public void ativar() {
        this.statusAtivo = "S";
    }

    /**
     * Desativa a categoria
     */
    public void desativar() {
        this.statusAtivo = "N";
    }
}
