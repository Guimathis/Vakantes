package com.DevProj.Vakantes.model.comunicacao;

import com.DevProj.Vakantes.model.entrevista.Entrevista;
import com.DevProj.Vakantes.model.entrevista.enums.StatusNotificacaoEmail;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comunicacao", schema = "vaga")
public class Comunicacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "entrevista_id")
    private Entrevista entrevista;

    @Column(length = 500, nullable = false)
    private String mensagem;

    @Column(name = "email_destino", nullable = false)
    private String email;

    @Column(name = "data_comunicacao", nullable = false)
    private LocalDateTime dataComunicacao;

    @Enumerated(EnumType.STRING)
    private StatusNotificacaoEmail statusEnvio;

    public Comunicacao() {
        this.dataComunicacao = LocalDateTime.now();
    }

    public Comunicacao(Entrevista entrevista, String mensagem, String email) {
        this.entrevista = entrevista;
        this.mensagem = mensagem;
        this.email = email;
        this.dataComunicacao = LocalDateTime.now();
        this.statusEnvio = StatusNotificacaoEmail.PENDENTE;
    }
    public Comunicacao(String mensagem, String email) {
        this.entrevista = null;
        this.mensagem = mensagem;
        this.email = email;
        this.dataComunicacao = LocalDateTime.now();
        this.statusEnvio = StatusNotificacaoEmail.PENDENTE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Entrevista getEntrevista() {
        return entrevista;
    }

    public void setEntrevista(Entrevista entrevista) {
        this.entrevista = entrevista;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDataComunicacao() {
        return dataComunicacao;
    }

    public void setDataComunicacao(LocalDateTime dataComunicacao) {
        this.dataComunicacao = dataComunicacao;
    }

    public StatusNotificacaoEmail getStatusEnvio() {
        return statusEnvio;
    }

    public void setStatusEnvio(StatusNotificacaoEmail statusEnvio) {
        this.statusEnvio = statusEnvio;
    }
}