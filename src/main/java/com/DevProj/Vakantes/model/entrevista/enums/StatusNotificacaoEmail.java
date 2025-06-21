package com.DevProj.Vakantes.model.entrevista.enums;

public enum StatusNotificacaoEmail {
    PENDENTE("Pendente"),
    ENVIADO("Enviado"),
    ERRO("Erro");

    private final String descricao;

    private StatusNotificacaoEmail(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
