package com.DevProj.Vakantes.model.empresa.enums;

public enum TipoPessoa {
    JURIDICA("Pessoa Jurídica"),
    FISICA("Pessoa Física");

    private final String descricao;

    TipoPessoa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}