package com.DevProj.Vakantes.model.vaga;

import com.DevProj.Vakantes.model.util.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "requisito", schema = "vaga")
public class Requisito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requisito_seq")
    @SequenceGenerator(name = "requisito_seq", sequenceName = "vaga.requisito_seq", allocationSize = 1)
    private Long id;

    @NotEmpty
    private String nome;

    @NotNull
    private Integer nivelMinimo; // 1-5 para indicar nível de proficiência

    @NotNull
    private Boolean obrigatorio;

    @ManyToOne
    @JoinColumn(name = "vaga_id")
    private Vaga vaga;

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

    public Requisito() {
        this.status = Status.ATIVO;
    }

    public Requisito(String nome, Integer nivelMinimo, Boolean obrigatorio, Vaga vaga) {
        this.nome = nome;
        this.nivelMinimo = nivelMinimo;
        this.obrigatorio = obrigatorio;
        this.vaga = vaga;
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


    public Integer getNivelMinimo() {
        return nivelMinimo;
    }

    public void setNivelMinimo(Integer nivelMinimo) {
        this.nivelMinimo = nivelMinimo;
    }

    public Boolean getObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
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
