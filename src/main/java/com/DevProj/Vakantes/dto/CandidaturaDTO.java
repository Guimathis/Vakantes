package com.DevProj.Vakantes.dto;

public class CandidaturaDTO {
    private Long id;
    private String nomeCandidato;

    public CandidaturaDTO(Long id, String nomeCandidato) {
        this.id = id;
        this.nomeCandidato = nomeCandidato;
    }

    public Long getId() {
        return id;
    }

    public String getNomeCandidato() {
        return nomeCandidato;
    }
}

