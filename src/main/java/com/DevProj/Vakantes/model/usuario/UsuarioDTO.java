package com.DevProj.Vakantes.model.usuario;

public class UsuarioDTO {
    private Long id;
    private String username;
    private String password;

    public UsuarioDTO() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
