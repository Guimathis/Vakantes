package com.DevProj.Vakantes.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UsuarioDTO {

    private Long id;
    private String nomeCompleto;
    private String login;
    private String senha;

}
