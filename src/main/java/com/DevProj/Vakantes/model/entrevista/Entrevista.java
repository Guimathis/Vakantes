package com.DevProj.Vakantes.model.entrevista;

import com.DevProj.Vakantes.model.comunicacao.Comunicacao;
import com.DevProj.Vakantes.model.entrevista.enums.StatusEntrevista;
import com.DevProj.Vakantes.model.entrevista.enums.StatusNotificacaoEmail;
import com.DevProj.Vakantes.model.vaga.Candidatura;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "entrevista", schema = "vaga")
public class Entrevista implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entrevista_seq")
    @SequenceGenerator(name = "entrevista_seq", sequenceName = "vaga.entrevista_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String local;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(length = 500)
    private String observacoes;

    @Enumerated(EnumType.STRING)
    private StatusEntrevista statusEntrevista;

    @OneToOne
    @JoinColumn(name = "candidatura_id", referencedColumnName = "id", nullable = false, unique = true)
    private Candidatura candidatura;

    @OneToMany(mappedBy = "entrevista")
    private List<Comunicacao> comunicacoes;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "criado_em", updatable = false)
    private Date criadoEm;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "atualizado_em")
    private Date atualizadoEm;

    public Entrevista(String local, String observacoes, LocalDateTime dataHora, Candidatura candidatura, List<Comunicacao> comunicacoes) {
        this.local = local;
        this.dataHora = dataHora;
        this.observacoes = observacoes;
        this.statusEntrevista = StatusEntrevista.AGENDADA;
        this.candidatura = candidatura;
        this.comunicacoes = comunicacoes;
    }

    @PrePersist
    protected void onCreate() {
        criadoEm = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = new Date();
    }

    public Entrevista() {
        this.statusEntrevista = StatusEntrevista.AGENDADA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Candidatura getCandidatura() {
        return candidatura;
    }

    public void setCandidatura(Candidatura candidatura) {
        this.candidatura = candidatura;
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

    public StatusEntrevista getStatusEntrevista() {
        return statusEntrevista;
    }

    public void setStatusEntrevista(StatusEntrevista statusEntrevista) {
        this.statusEntrevista = statusEntrevista;
    }

    public List<Comunicacao> getComunicacoes() {
        return comunicacoes;
    }

    public void setComunicacoes(List<Comunicacao> comunicacoes) {
        this.comunicacoes = comunicacoes;
    }
}
