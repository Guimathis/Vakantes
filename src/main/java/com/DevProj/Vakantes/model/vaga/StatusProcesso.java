package com.DevProj.Vakantes.model.vaga;

public enum StatusProcesso {
    ABERTA("Aberta"),
    SELECAO("Seleção"),
    ENTREVISTA("Entrevistas"),
    FINALIZADA("Finalizada"),
    CANCELADA("Cancelada");

    private String descricao;

    StatusProcesso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getBadgeClass() {
        return switch (this) {
            case ABERTA -> "aberta";
            case SELECAO -> "em-selecao";
            case ENTREVISTA -> "entrevistas";
            case FINALIZADA -> "finalizada";
            case CANCELADA -> "cancelada";
            default -> "bg-secondary";
        };
    }
}
