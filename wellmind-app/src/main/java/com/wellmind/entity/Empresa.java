package com.wellmind.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EMPRESA", indexes = {
        @Index(name = "idx_empresa_cnpj", columnList = "CNPJ")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "usuarios")
@EqualsAndHashCode(of = "idEmpresa")
public class Empresa {

    @Id
    @SequenceGenerator(
            name = "seq_empresa_gen",
            sequenceName = "seq_empresa",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_empresa_gen"
    )
    @Column(name = "ID_EMPRESA")
    private Long idEmpresa;

    @Column(name = "NOME_EMPRESA", nullable = false, length = 200)
    private String nomeEmpresa;

    @Column(name = "CNPJ", nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(name = "ENDERECO", length = 300)
    private String endereco;

    @Column(name = "TELEFONE", length = 20)
    private String telefone;

    @Column(name = "EMAIL_CONTATO", length = 100)
    private String emailContato;

    @CreationTimestamp
    @Column(name = "DATA_CADASTRO", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @UpdateTimestamp
    @Column(name = "ULTIMA_ATUALIZACAO")
    private LocalDateTime ultimaAtualizacao;

    @Column(name = "STATUS_ATIVO", length = 1)
    private String statusAtivo;

    // Relacionamentos

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UsuarioEmpresa> usuarios = new ArrayList<>();

    // Lifecycle Callbacks

    @PrePersist
    protected void onCreate() {
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
        if (statusAtivo == null) {
            statusAtivo = "S";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ultimaAtualizacao = LocalDateTime.now();
    }

    // Business Methods

    /**
     * Verifica se a empresa está ativa
     */
    public boolean isAtiva() {
        return "S".equals(this.statusAtivo);
    }

    /**
     * Ativa a empresa
     */
    public void ativar() {
        this.statusAtivo = "S";
    }

    /**
     * Desativa a empresa
     */
    public void desativar() {
        this.statusAtivo = "N";
    }

    /**
     * Adiciona vínculo com usuário
     */
    public void adicionarUsuario(UsuarioEmpresa vinculo) {
        usuarios.add(vinculo);
        vinculo.setEmpresa(this);
    }

    /**
     * Retorna total de colaboradores ativos
     */
    public long getTotalColaboradoresAtivos() {
        return usuarios.stream()
                .filter(ue -> "A".equals(ue.getStatusVinculo()))
                .count();
    }

    /**
     * Formata CNPJ para exibição
     * 12.345.678/0001-90
     */
    public String getCnpjFormatado() {
        if (cnpj == null || cnpj.length() != 14) {
            return cnpj;
        }
        return cnpj.substring(0, 2) + "." +
                cnpj.substring(2, 5) + "." +
                cnpj.substring(5, 8) + "/" +
                cnpj.substring(8, 12) + "-" +
                cnpj.substring(12);
    }
}
