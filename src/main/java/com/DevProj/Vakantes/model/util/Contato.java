package com.DevProj.Vakantes.model.util;

import jakarta.persistence.*;


@Entity
@Table(name = "contato", schema = "util")

public class Contato {

    @Id
    @SequenceGenerator(name = "contato_id", sequenceName = "util.contato_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "contato_id")
    private Long id;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "email_contato")
    private String emailContato;

    public Contato() {
    }

    public Contato(String telefone, String emailContato) {
        this.telefone = telefone;
        this.emailContato = emailContato;
    }

    public Long getId() {
        return id;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
