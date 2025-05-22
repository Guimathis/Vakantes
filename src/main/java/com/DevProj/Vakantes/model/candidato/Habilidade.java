package com.DevProj.Vakantes.model.candidato;

import com.DevProj.Vakantes.model.util.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "habilidade", schema = "candidato")
public class Habilidade implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habilidade_seq")
    @SequenceGenerator(name = "habilidade_seq", sequenceName = "candidato.habilidade_seq", allocationSize = 1)
    private Long id;
    
    @NotEmpty
    private String nome;
    
    @NotNull
    private Integer nivel; // 1-5 para indicar nível de proficiência
    
    @ManyToOne
    @JoinColumn(name = "candidato_id")
    private Candidato candidato;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
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
        status = Status.ATIVO;
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = new Date();
    }
    
    public Habilidade() {
        this.status = Status.ATIVO;
    }
    
    public Habilidade(String nome, Integer nivel, Candidato candidato) {
        this.nome = nome;
        this.nivel = nivel;
        this.candidato = candidato;
        this.status = Status.ATIVO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Candidato getCandidato() {
        return candidato;
    }

    public void setCandidato(Candidato candidato) {
        this.candidato = candidato;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getCriadoEm() {
        return criadoEm;
    }

    public Date getAtualizadoEm() {
        return atualizadoEm;
    }
}