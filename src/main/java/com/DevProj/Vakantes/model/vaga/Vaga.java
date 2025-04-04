package com.DevProj.Vakantes.model.vaga;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.util.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "vaga", schema = "vaga")
public class Vaga implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vaga_seq")
	@SequenceGenerator(name = "vaga_seq", sequenceName = "vaga.vaga_seq", allocationSize = 1)
	private long codigo;
	
	@NotEmpty
	private String nome;
	
	@NotEmpty
	private String descricao;
	
	@NotEmpty
	private String data;
	
	private BigDecimal salario;

	@ManyToMany
	@JoinTable(
			name = "vaga_candidato", schema = "vaga",
			joinColumns = @JoinColumn(name = "vaga_id"),
			inverseJoinColumns = @JoinColumn(name = "candidato_id")
	)
	private List<Candidato> candidatos = new ArrayList<>();

	@ManyToOne
	private Cliente cliente;

	@Column(nullable = false)
	private Status status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "criado_em", updatable = false)
	private Date criadoEm;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "atualizado_em", nullable = true)
	private Date atualizadoEm;

	@PrePersist
	protected void onCreate() {
		criadoEm = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		atualizadoEm = new Date();
	}

	public Vaga() {

		this.cliente = new Cliente();
	}

	public Vaga(long codigo, String nome, String descricao, String data, BigDecimal salario, List<Candidato> candidatos,
			Cliente cliente) {
		this.codigo = codigo;
		this.nome = nome;
		this.descricao = descricao;
		this.data = data;
		this.salario = salario;
		this.cliente = cliente;
		this.status = Status.ATIVO;
	}

	public Vaga(VagaDTO vagaDTO, Cliente cliente) {
		this.codigo = vagaDTO.getCodigo();
		this.nome = vagaDTO.getNome();
		this.descricao = vagaDTO.getDescricao();
		this.data = vagaDTO.getData();
		this.salario = vagaDTO.getSalario();
		this.cliente = cliente;
		this.status = Status.ATIVO;
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

	public long getCodigo() {
		return codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

	public List<Candidato> getCandidatos() {
		return candidatos;
	}

	public void setCandidatos(List<Candidato> candidatos) {
		this.candidatos = candidatos;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
