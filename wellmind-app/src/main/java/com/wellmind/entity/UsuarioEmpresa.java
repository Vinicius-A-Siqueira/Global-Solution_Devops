package com.wellmind.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "USUARIO_EMPRESA",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_usuario_empresa",
                        columnNames = {"ID_USUARIO", "ID_EMPRESA"}
                )
        },
        indexes = {
                @Index(name = "idx_ue_usuario", columnList = "ID_USUARIO"),
                @Index(name = "idx_ue_empresa", columnList = "ID_EMPRESA"),
                @Index(name = "idx_ue_status", columnList = "STATUS_VINCULO")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"usuario", "empresa"})
@EqualsAndHashCode(of = "idUsuarioEmpresa")
public class UsuarioEmpresa {

    @Id
    @SequenceGenerator(
            name = "seq_usuario_empresa_gen",
            sequenceName = "seq_usuario_empresa",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_usuario_empresa_gen"
    )
    @Column(name = "ID_USUARIO_EMPRESA")
    private Long idUsuarioEmpresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EMPRESA", nullable = false)
    private Empresa empresa;

    @Column(name = "CARGO", length = 100)
    private String cargo;

    @Column(name = "DATA_VINCULO", nullable = false)
    private LocalDate dataVinculo;

    @Column(name = "DATA_DESVINCULO")
    private LocalDate dataDesvinculo;

    @Column(name = "STATUS_VINCULO", length = 1)
    private String statusVinculo; // 'A' = Ativo, 'I' = Inativo

    @CreationTimestamp
    @Column(name = "DATA_CADASTRO", updatable = false)
    private LocalDateTime dataCadastro;

    // Lifecycle Callbacks

    @PrePersist
    protected void onCreate() {
        if (dataVinculo == null) {
            dataVinculo = LocalDate.now();
        }
        if (statusVinculo == null) {
            statusVinculo = "A";
        }
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
    }

    // Business Methods

    /**
     * Verifica se o vínculo está ativo
     */
    public boolean isVinculoAtivo() {
        return "A".equals(this.statusVinculo) && dataDesvinculo == null;
    }

    /**
     * Ativa o vínculo
     */
    public void ativarVinculo() {
        this.statusVinculo = "A";
        this.dataDesvinculo = null;
    }

    /**
     * Desativa o vínculo
     */
    public void desativarVinculo() {
        this.statusVinculo = "I";
        this.dataDesvinculo = LocalDate.now();
    }

    /**
     * Calcula tempo de vínculo em anos
     */
    public int getTempoVinculoAnos() {
        LocalDate dataFim = dataDesvinculo != null ? dataDesvinculo : LocalDate.now();
        return dataFim.getYear() - dataVinculo.getYear();
    }
}
