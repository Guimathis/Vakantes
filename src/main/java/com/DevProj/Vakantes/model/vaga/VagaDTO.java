package com.DevProj.Vakantes.model.vaga;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.entrevista.Entrevista;
import com.DevProj.Vakantes.model.vaga.enums.StatusProcesso;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VagaDTO {
    private long codigo;

    @NotEmpty(message = "Nome da vaga não pode estar vazio.")
    private String nome;

    private Cliente cliente;

    @NotEmpty(message = "Descrição da vaga não pode estar vazia.")
    private String descricao;

    @NotEmpty(message = "Selecione a data de expiração da vaga.")
    private String data;

    @NotNull(message = "Salário não pode ser nulo.")
    private BigDecimal salario;

    @NotNull(message = "Selecione um cliente.")
    private Long idCliente;

    private String nivelExperiencia; // Júnior, Pleno, Sênior

    private String tipoContrato; // CLT, PJ, Estágio

    private String modalidadeTrabalho; // Presencial, Remoto, Híbrido

    private String localizacao; // Cidade/Estado

    private List<Requisito> requisitos = new ArrayList<>();

    private List<Candidato> candidatos = new ArrayList<>();

    private StatusProcesso statusProcesso;

    private List<Candidatura> candidaturas = new ArrayList<>();

    public VagaDTO() {
    }

    public VagaDTO(Vaga vaga) {
        this.codigo = vaga.getCodigo();
        this.nome = vaga.getNome();
        this.cliente = vaga.getCliente();
        this.descricao = vaga.getDescricao();
        this.data = vaga.getData();
        this.salario = vaga.getSalario();
        this.nivelExperiencia = vaga.getNivelExperiencia();
        this.tipoContrato = vaga.getTipoContrato();
        this.modalidadeTrabalho = vaga.getModalidadeTrabalho();
        this.localizacao = vaga.getLocalizacao();
        this.idCliente = vaga.getCliente().getId();
        this.requisitos = vaga.getRequisitos();
        this.statusProcesso = vaga.getStatusProcesso();
        this.candidaturas = vaga.getCandidaturas();
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Candidatura> getCandidaturas() {
        return candidaturas;
    }

    public void setCandidaturas(List<Candidatura> candidaturas) {
        this.candidaturas = candidaturas;
    }

    public List<Candidato> getCandidatos() {
        return candidatos;
    }

    public void setCandidatos(List<Candidato> candidatos) {
        this.candidatos = candidatos;
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

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
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

    public List<Requisito> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(List<Requisito> requisitos) {
        this.requisitos = requisitos;
    }
}
