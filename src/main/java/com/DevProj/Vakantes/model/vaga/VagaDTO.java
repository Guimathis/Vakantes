package com.DevProj.Vakantes.model.vaga;

import com.DevProj.Vakantes.model.empresa.Cliente;

import java.math.BigDecimal;

public class VagaDTO {
    private long codigo;

    private String nome;

    private String descricao;

    private String data;

    private BigDecimal salario;

    private Long idCliente;


    public VagaDTO() {
    }

    public VagaDTO(String nome, String descricao, String data, BigDecimal salario, Long idCliente) {
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.salario = salario;
        this.idCliente = idCliente;
    }

    public VagaDTO(Vaga vaga) {
        this.codigo = vaga.getCodigo();
        this.nome = vaga.getNome();
        this.descricao = vaga.getDescricao();
        this.data = vaga.getData();
        this.salario = vaga.getSalario();
        this.idCliente = vaga.getCliente().getId();
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
}
