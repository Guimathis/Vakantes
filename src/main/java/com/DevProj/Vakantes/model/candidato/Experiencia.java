package com.DevProj.Vakantes.model.candidato;

import com.DevProj.Vakantes.model.util.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "experiencia", schema = "candidato")
public class Experiencia implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "experiencia_seq")
    @SequenceGenerator(name = "experiencia_seq", sequenceName = "candidato.experiencia_seq", allocationSize = 1)
    private Long id;
    
    @NotEmpty
    private String empresa;
    
    @NotEmpty
    private String cargo;
    
    private String descricao;
    
    @NotNull
    private LocalDate dataInicio;
    
    private LocalDate dataFim;
    
    @NotNull
    private Boolean atual;
    
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
    
    public Experiencia() {
        this.status = Status.ATIVO;
    }
    
    public Experiencia(String empresa, String cargo, String descricao, LocalDate dataInicio, 
                      LocalDate dataFim, Boolean atual, Candidato candidato) {
        this.empresa = empresa;
        this.cargo = cargo;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.atual = atual;
        this.candidato = candidato;
        this.status = Status.ATIVO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean getAtual() {
        return atual;
    }

    public void setAtual(Boolean atual) {
        this.atual = atual;
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