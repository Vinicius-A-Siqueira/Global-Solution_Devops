package com.wellmind.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "REGISTRO_BEMESTAR", indexes = {
        @Index(name = "idx_rb_usuario", columnList = "ID_USUARIO"),
        @Index(name = "idx_rb_data", columnList = "DATA_REGISTRO")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "usuario")
@EqualsAndHashCode(of = "idRegistro")
public class RegistroBemestar {

    @Id
    @SequenceGenerator(
            name = "seq_registro_bemestar_gen",
            sequenceName = "seq_registro_bemestar",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_registro_bemestar_gen"
    )
    @Column(name = "ID_REGISTRO")
    private Long idRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @CreationTimestamp
    @Column(name = "DATA_REGISTRO", nullable = false, updatable = false)
    private LocalDateTime dataRegistro;

    @Column(name = "NIVEL_HUMOR", nullable = false)
    private Integer nivelHumor; // 1-10

    @Column(name = "NIVEL_ESTRESSE", nullable = false)
    private Integer nivelEstresse; // 1-10

    @Column(name = "NIVEL_ENERGIA", nullable = false)
    private Integer nivelEnergia; // 1-10

    @Column(name = "HORAS_SONO")
    private Double horasSono; // 0-24

    @Column(name = "QUALIDADE_SONO")
    private Integer qualidadeSono; // 1-10

    @Column(name = "OBSERVACOES", length = 1000)
    private String observacoes;

    // Lifecycle Callbacks

    @PrePersist
    protected void onCreate() {
        if (dataRegistro == null) {
            dataRegistro = LocalDateTime.now();
        }
    }

    // Business Methods

    /**
     * Calcula média geral de bem-estar
     */
    public double getMediaBemestar() {
        int total = nivelHumor + (11 - nivelEstresse) + nivelEnergia;
        if (qualidadeSono != null) {
            total += qualidadeSono;
            return total / 4.0;
        }
        return total / 3.0;
    }

    /**
     * Verifica se há alerta de estresse alto
     */
    public boolean isEstresseAlto() {
        return nivelEstresse != null && nivelEstresse >= 8;
    }

    /**
     * Verifica se há alerta de humor baixo
     */
    public boolean isHumorBaixo() {
        return nivelHumor != null && nivelHumor <= 3;
    }

    /**
     * Verifica se há alerta de sono inadequado
     */
    public boolean isSonoInadequado() {
        return horasSono != null && (horasSono < 6 || horasSono > 9);
    }

    /**
     * Verifica se precisa gerar alerta
     */
    public boolean precisaAlerta() {
        return isEstresseAlto() || isHumorBaixo() || isSonoInadequado();
    }

    /**
     * Classifica nível de bem-estar
     */
    public String getClassificacaoBemestar() {
        double media = getMediaBemestar();
        if (media >= 8) return "EXCELENTE";
        if (media >= 6) return "BOM";
        if (media >= 4) return "REGULAR";
        if (media >= 2) return "RUIM";
        return "CRÍTICO";
    }
}
