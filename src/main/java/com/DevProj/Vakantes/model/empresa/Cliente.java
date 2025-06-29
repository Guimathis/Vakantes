package com.DevProj.Vakantes.model.empresa;

import com.DevProj.Vakantes.model.empresa.enums.TipoPessoa;
import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.model.util.Contato;
import com.DevProj.Vakantes.model.util.Endereco;
import com.DevProj.Vakantes.model.util.enums.Status;
import com.DevProj.Vakantes.model.vaga.Vaga;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity()
@Table(name = "cliente", schema = "empresa")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_seq")
    @SequenceGenerator(name = "cliente_seq", sequenceName = "empresa.cliente_seq", allocationSize = 1)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoPessoa tipoPessoa;

    private String documento; // Pode ser CPF ou CNPJ

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Endereco> enderecos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contato_id")
    private Contato contato;

    @OneToOne
    @JoinColumn(name = "responsavel_id", unique = false)
    private Usuario usuarioResponsavel;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.REMOVE)
    private List<Vaga> vagas;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "criado_em", updatable = false)
    private Date criadoEm;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "atualizado_em", nullable = true)
    private Date atualizadoEm;

    public Cliente(String nome, TipoPessoa tipoPessoa, String s, Status status) {
        this.nome = nome;
        this.tipoPessoa = tipoPessoa;
        this.documento = s;
        this.status = status;
        this.endereco = new Endereco();
        this.contato = new Contato();
    }

    @PrePersist
    protected void onCreate() {
        criadoEm = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = new Date();
    }

    public List<Endereco> getEnderecos() {
        return Collections.singletonList(endereco);
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public Cliente() {
        status = Status.ATIVO;
    }

    public Date getCriadoEm() {
        return criadoEm;
    }

    public Date getAtualizadoEm() {
        return atualizadoEm;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoPessoa getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoa tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public Usuario getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    public void setUsuarioResponsavel(Usuario usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }

    public List<Vaga> getVagas() {
        return vagas;
    }

    public void setVagas(List<Vaga> vagas) {
        this.vagas = vagas;
    }
}