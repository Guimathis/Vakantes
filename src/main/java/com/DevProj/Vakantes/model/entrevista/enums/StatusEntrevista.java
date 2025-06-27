package com.DevProj.Vakantes.model.entrevista.enums;

public enum StatusEntrevista {
    AGENDADA("Confirmada"),
    CANCELADA("Cancelada"),
    REALIZADA("Realizada");

    private final String descricao;

    private StatusEntrevista(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
