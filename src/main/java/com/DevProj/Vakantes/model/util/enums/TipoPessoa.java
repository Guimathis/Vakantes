package com.DevProj.Vakantes.model.util.enums;

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