package com.DevProj.Vakantes.model;

import jakarta.persistence.*;


@Entity
@SequenceGenerator(name = "contato_id", sequenceName = "vakantes.contato_id_seq", allocationSize = 1, initialValue = 1)
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "contato_id")
    private Long id;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "email_contato", unique = true)
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
