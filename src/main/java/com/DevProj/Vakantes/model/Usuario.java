package com.DevProj.Vakantes.model;

import com.DevProj.Vakantes.model.enums.UserRole;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@SequenceGenerator(name = "usuario_id", sequenceName = "vakantes.usuario_id_seq", allocationSize = 1, initialValue = 1)
public class Usuario{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "usuario_id")
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "nome_completo", length = 100)
    private String nomeCompleto;

    @Column(name = "Email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "senha", nullable = false, unique = true)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @OneToOne(cascade = CascadeType.ALL)
    private Contato contato;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "criado_em", updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "atualizado_em", nullable = true)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public Usuario() {
        this.contato = new Contato();
    }

    public Usuario(String nomeCompleto, String email, String senha, UserRole userRole, Contato contato) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
        this.userRole = userRole;
        this.contato = contato;
    }

    public Long getId() {
        return id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
