package com.DevProj.Vakantes.model.vaga;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.entrevista.Entrevista;
import com.DevProj.Vakantes.model.util.enums.Status;
import com.DevProj.Vakantes.model.vaga.enums.StatusProcesso;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "vaga", schema = "vaga")
public class Vaga implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vaga_seq")
    @SequenceGenerator(name = "vaga_seq", sequenceName = "vaga.vaga_seq", allocationSize = 1)
    private long codigo;

    @NotEmpty
    private String nome;

    @NotEmpty
    private String descricao;

    @NotEmpty
    private String data;

    @NotNull
    private BigDecimal salario;

    @OneToMany(mappedBy = "vaga", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidatura> candidaturas = new ArrayList<>();

    @OneToMany(mappedBy = "vaga", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Requisito> requisitos = new ArrayList<>();

    private String nivelExperiencia; // Júnior, Pleno, Sênior

    private String tipoContrato; // CLT, PJ, Estágio

    private String modalidadeTrabalho; // Presencial, Remoto, Híbrido

    private String localizacao; // Cidade/Estado

    @ManyToOne
    private Cliente cliente;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusProcesso statusProcesso;

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

    public Vaga() {
        this.cliente = new Cliente();
        this.status = Status.ATIVO;
        this.statusProcesso = StatusProcesso.ABERTA;
    }

    public Vaga(long codigo, String nome, String descricao, String data, BigDecimal salario, List<Candidato> candidatos,
                Cliente cliente) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.salario = salario;
        this.cliente = cliente;
        this.status = Status.ATIVO;
        this.statusProcesso = StatusProcesso.ABERTA;
    }

    public Vaga(VagaDTO vagaDTO, Cliente cliente) {
        this.codigo = vagaDTO.getCodigo();
        this.nome = vagaDTO.getNome();
        this.descricao = vagaDTO.getDescricao();
        this.data = vagaDTO.getData();
        this.salario = vagaDTO.getSalario();
        this.nivelExperiencia = vagaDTO.getNivelExperiencia();
        this.tipoContrato = vagaDTO.getTipoContrato();
        this.modalidadeTrabalho = vagaDTO.getModalidadeTrabalho();
        this.localizacao = vagaDTO.getLocalizacao();
        this.cliente = cliente;
        this.status = Status.ATIVO;
        this.statusProcesso = vagaDTO.getStatusProcesso() == null ? StatusProcesso.ABERTA : vagaDTO.getStatusProcesso();
    }

    public List<Candidatura> getCandidaturas() {
        return candidaturas;
    }

    public List<Candidato> getCandidatos() {
        return candidaturas.stream()
                .map(Candidatura::getCandidato)
                .toList();
    }

    public void setCandidaturas(List<Candidatura> candidaturas) {
        this.candidaturas = candidaturas;
    }

    public Date getCriadoEm() {
        return criadoEm;
    }

    public Date getAtualizadoEm() {
        return atualizadoEm;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public StatusProcesso getStatusProcesso() {
        return statusProcesso;
    }

    public void setStatusProcesso(StatusProcesso statusProcesso) {
        this.statusProcesso = statusProcesso;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Requisito> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(List<Requisito> requisitos) {
        this.requisitos = requisitos;
    }

    public String getNivelExperiencia() {
        return nivelExperiencia;
    }

    public void setNivelExperiencia(String nivelExperiencia) {
        this.nivelExperiencia = nivelExperiencia;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getModalidadeTrabalho() {
        return modalidadeTrabalho;
    }

    public void setModalidadeTrabalho(String modalidadeTrabalho) {
        this.modalidadeTrabalho = modalidadeTrabalho;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public void adicionarCandidatura(Candidatura candidatura) {
        this.candidaturas.add(candidatura);
    }
}
