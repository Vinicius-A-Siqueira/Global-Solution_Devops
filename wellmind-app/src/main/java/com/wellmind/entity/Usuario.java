package com.wellmind.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "USUARIO", indexes = {
        @Index(name = "idx_usuario_email", columnList = "EMAIL"),
        @Index(name = "idx_usuario_status", columnList = "STATUS_ATIVO")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"empresas", "registrosBemestar"})
@EqualsAndHashCode(of = "idUsuario")
public class Usuario implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "seq_usuario_gen",
            sequenceName = "seq_usuario",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_usuario_gen"
    )
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "SENHA_HASH", nullable = false, length = 255)
    private String senhaHash;

    @Column(name = "DATA_NASCIMENTO", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "GENERO", length = 20)
    private String genero;

    @Column(name = "TELEFONE", length = 20)
    private String telefone;

    @CreationTimestamp
    @Column(name = "DATA_CADASTRO", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "STATUS_ATIVO", length = 1)
    private String statusAtivo;

    @UpdateTimestamp
    @Column(name = "ULTIMA_ATUALIZACAO")
    private LocalDateTime ultimaAtualizacao;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UsuarioEmpresa> empresas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<RegistroBemestar> registrosBemestar = new ArrayList<>();

    // ---- Spring Security UserDetails ----

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senhaHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return "S".equals(statusAtivo);
    }

    @Override
    public boolean isAccountNonLocked() {
        return "S".equals(statusAtivo);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return "S".equals(statusAtivo);
    }

    @Override
    public boolean isEnabled() {
        return "S".equals(statusAtivo);
    }

    // ---- Métodos utilitários do seu modelo ----

    @PrePersist
    protected void onCreate() {
        if (dataCadastro == null) dataCadastro = LocalDateTime.now();
        if (statusAtivo == null) statusAtivo = "S";
    }

    @PreUpdate
    protected void onUpdate() {
        ultimaAtualizacao = LocalDateTime.now();
    }

    public boolean isAtivo() {
        return "S".equals(this.statusAtivo);
    }

    public void ativar() {
        this.statusAtivo = "S";
    }

    public void desativar() {
        this.statusAtivo = "N";
    }

    public int getIdade() {
        if (dataNascimento == null) return 0;
        return LocalDate.now().getYear() - dataNascimento.getYear();
    }

    public void adicionarEmpresa(UsuarioEmpresa vinculo) {
        empresas.add(vinculo);
        vinculo.setUsuario(this);
    }

    public void adicionarRegistroBemestar(RegistroBemestar registro) {
        registrosBemestar.add(registro);
        registro.setUsuario(this);
    }
}
