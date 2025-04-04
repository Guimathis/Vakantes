package com.DevProj.Vakantes.model.candidato;

import com.DevProj.Vakantes.model.enums.SituacaoCandidato;
import com.DevProj.Vakantes.model.util.Contato;
import com.DevProj.Vakantes.model.util.Endereco;
import com.DevProj.Vakantes.model.util.Status;
import com.DevProj.Vakantes.model.vaga.Vaga;

import java.util.ArrayList;
import java.util.List;

public class CandidatoDTO {

    private Long id;

    private String rg;
    private String cpf;
    private String nomeCandidato;
    private List<Vaga> vagas = new ArrayList<>();
    private String dataNascimento;
    private Contato contato;
    private Endereco endereco;
    private SituacaoCandidato situacao;
    private Status status;

    public CandidatoDTO() {
    }

    public CandidatoDTO(String rg, String cpf, String nomeCandidato, List<Vaga> vagas,
                        String dataNascimento, Contato contato, Endereco endereco) {
        this.rg = rg;
        this.cpf = cpf;
        this.nomeCandidato = nomeCandidato;
        this.vagas = vagas;
        this.dataNascimento = dataNascimento;
        this.contato = contato;
        this.endereco = endereco;
    }

    public CandidatoDTO(Candidato candidato) {
        this.id = candidato.getId();
        this.rg = candidato.getRg();
        this.cpf = candidato.getCpf();
        this.nomeCandidato = candidato.getNomeCandidato();
        this.vagas = candidato.getVagas();
        this.dataNascimento = candidato.getDataNascimento().toString();
        this.contato = candidato.getContato();
        this.endereco = candidato.getEndereco();
        this.situacao = candidato.getSituacao();
        this.status = candidato.getStatus();
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNomeCandidato() {
        return nomeCandidato;
    }

    public void setNomeCandidato(String nomeCandidato) {
        this.nomeCandidato = nomeCandidato;
    }

    public List<Vaga> getVagas() {
        return vagas;
    }

    public void setVagas(List<Vaga> vagas) {
        this.vagas = vagas;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public SituacaoCandidato getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoCandidato situacao) {
        this.situacao = situacao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
