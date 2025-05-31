package com.DevProj.Vakantes.model.vaga;

import com.DevProj.Vakantes.model.candidato.Candidato;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "candidatura", schema = "vaga")
public class Candidatura implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Vaga vaga;

    @ManyToOne
    private Candidato candidato;

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

    public Candidatura() {
    }

    public Candidatura(Vaga vaga, Candidato candidato) {
        this.vaga = vaga;
        this.candidato = candidato;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }

    public Candidato getCandidato() {
        return candidato;
    }

    public void setCandidato(Candidato candidato) {
        this.candidato = candidato;
    }

    public Date getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Date criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Date getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(Date atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}
