package com.wellmind.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "PROFISSIONAL_SAUDE", indexes = {
        @Index(name = "idx_prof_crp_crm", columnList = "CRP_CRM"),
        @Index(name = "idx_prof_especialidade", columnList = "ESPECIALIDADE"),
        @Index(name = "idx_prof_disponivel", columnList = "DISPONIVEL")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "idProfissional")
public class ProfissionalSaude {

    @Id
    @SequenceGenerator(
            name = "seq_profissional_saude_gen",
            sequenceName = "seq_profissional_saude",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_profissional_saude_gen"
    )
    @Column(name = "ID_PROFISSIONAL")
    private Long idProfissional;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "ESPECIALIDADE", nullable = false, length = 100)
    private String especialidade;

    @Column(name = "CRP_CRM", nullable = false, unique = true, length = 20)
    private String crpCrm;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "TELEFONE", length = 20)
    private String telefone;

    @Column(name = "DISPONIVEL", length = 1)
    private String disponivel; // 'S' = Sim, 'N' = Não

    @Column(name = "HORARIO_ATENDIMENTO", length = 200)
    private String horarioAtendimento;

    @Column(name = "VALOR_CONSULTA")
    private Double valorConsulta;

    @Column(name = "DESCRICAO", length = 1000)
    private String descricao;

    @Column(name = "FOTO_URL", length = 500)
    private String fotoUrl;

    @CreationTimestamp
    @Column(name = "DATA_CADASTRO", updatable = false)
    private LocalDateTime dataCadastro;

    @UpdateTimestamp
    @Column(name = "ULTIMA_ATUALIZACAO")
    private LocalDateTime ultimaAtualizacao;

    @Column(name = "STATUS_ATIVO", length = 1)
    private String statusAtivo;

    // Lifecycle Callbacks

    @PrePersist
    protected void onCreate() {
        if (disponivel == null) {
            disponivel = "S";
        }
        if (statusAtivo == null) {
            statusAtivo = "S";
        }
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ultimaAtualizacao = LocalDateTime.now();
    }

    // Business Methods

    /**
     * Verifica se o profissional está disponível
     */
    public boolean isDisponivel() {
        return "S".equals(this.disponivel) && "S".equals(this.statusAtivo);
    }

    /**
     * Marca como disponível
     */
    public void marcarDisponivel() {
        this.disponivel = "S";
    }

    /**
     * Marca como indisponível
     */
    public void marcarIndisponivel() {
        this.disponivel = "N";
    }

    /**
     * Verifica se o profissional está ativo
     */
    public boolean isAtivo() {
        return "S".equals(this.statusAtivo);
    }

    /**
     * Ativa o profissional
     */
    public void ativar() {
        this.statusAtivo = "S";
    }

    /**
     * Desativa o profissional
     */
    public void desativar() {
        this.statusAtivo = "N";
        this.disponivel = "N";
    }

    /**
     * Retorna tipo de registro profissional
     */
    public String getTipoRegistro() {
        if (crpCrm == null) return "DESCONHECIDO";
        return crpCrm.toUpperCase().startsWith("CRP") ? "PSICÓLOGO" : "MÉDICO";
    }

    /**
     * Formata valor da consulta
     */
    public String getValorConsultaFormatado() {
        if (valorConsulta == null) return "A combinar";
        return String.format("R$ %.2f", valorConsulta);
    }
}
