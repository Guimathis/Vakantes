package com.DevProj.Vakantes.model;

import com.DevProj.Vakantes.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.ToString;
import java.util.Date;


@ToString
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "usuario_id", sequenceName = "vakantes.usuario_id_seq", allocationSize = 1, initialValue = 1)
public class Usuario{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "usuario_id")
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "nome_completo", length = 100)
    private String nomeCompleto;

    @Column(name = "login", nullable = false, length = 50, unique = true)
    private String login;

    @Column(name = "senha", nullable = false, unique = true)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @OneToOne
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
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
