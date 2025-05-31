package com.DevProj.Vakantes.model.candidato;

import com.DevProj.Vakantes.model.util.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "formacao", schema = "candidato")
public class Formacao implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "formacao_seq")
    @SequenceGenerator(name = "formacao_seq", sequenceName = "candidato.formacao_seq", allocationSize = 1)
    private Long id;
    
    @NotEmpty
    private String instituicao;
    
    @NotEmpty
    private String curso;
    
    @NotEmpty
    private String nivel; // Técnico, Graduação, Pós-graduação, etc.
    
    @NotNull
    private LocalDate dataInicio;
    
    private LocalDate dataFim;
    
    @NotNull
    private Boolean concluido;
    
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
    
    public Formacao() {
        this.status = Status.ATIVO;
    }
    
    public Formacao(String instituicao, String curso, String nivel, LocalDate dataInicio, 
                   LocalDate dataFim, Boolean concluido, Candidato candidato) {
        this.instituicao = instituicao;
        this.curso = curso;
        this.nivel = nivel;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.concluido = concluido;
        this.candidato = candidato;
        this.status = Status.ATIVO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
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

    public Boolean getConcluido() {
        return concluido;
    }

    public void setConcluido(Boolean concluido) {
        this.concluido = concluido;
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