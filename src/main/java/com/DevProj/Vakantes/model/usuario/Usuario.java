package com.DevProj.Vakantes.model.usuario;

import com.DevProj.Vakantes.model.enums.UserRole;
import com.DevProj.Vakantes.model.util.Contato;
import com.DevProj.Vakantes.model.util.Status;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "usuario", schema = "usuario")
public class Usuario{

    @Id
    @SequenceGenerator(name = "usuario_id", sequenceName = "usuario.usuario_id_seq", allocationSize = 1, initialValue = 1)
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

    @Column(nullable = false)
    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "criado_em", updatable = false)
    private Date criadoEm;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "atualizado_em", nullable = true)
    private Date atualizadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = new Date();
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



    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public String getPrimeiroNome() {
        return nomeCompleto.split("\\s+")[0];
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

    public Date getCriadoEm() {
        return criadoEm;
    }

    public Date getAtualizadoEm() {
        return atualizadoEm;
    }
}
