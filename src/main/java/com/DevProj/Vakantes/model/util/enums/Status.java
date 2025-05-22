package com.DevProj.Vakantes.model.util.enums;

public enum Status {
    ATIVO(1, "ATIVO"),
    INATIVO(2, "INATIVO");

    private final Integer codigo;
    private final String descricao;

    Status(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static Status toEnum(Integer code){
        if(code == null)
            return null;
        for (Status x : Status.values()){
            if(code.equals(x.getCodigo()))
                return x;
        }
        throw new IllegalArgumentException("Invalid code " + code);
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}
