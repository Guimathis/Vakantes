package com.DevProj.Vakantes.model.candidato;

import com.DevProj.Vakantes.model.util.enums.SituacaoCandidato;
import com.DevProj.Vakantes.model.util.Contato;
import com.DevProj.Vakantes.model.util.Endereco;
import com.DevProj.Vakantes.model.util.Status;
import com.DevProj.Vakantes.model.vaga.Vaga;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "candidato", schema = "candidato")
public class Candidato {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidato_seq")
	@SequenceGenerator(name = "candidato_seq", sequenceName = "candidato.candidato_seq", allocationSize = 1)
	private Long id;

	@Column(unique = true, nullable = false, length = 14)
	@NotBlank(message = "O RG não pode estar em branco")
	private String rg;

	@Column(unique = true, nullable = false, length = 14)
	@NotBlank(message = "O CPF é obrigatório")
	private String cpf;

	@Column(nullable = false)
	@NotBlank(message = "O nome do candidato é obrigatório")
	private String nomeCandidato;

	@ManyToMany(mappedBy = "candidatos")
	private List<Vaga> vagas = new ArrayList<>();

	@OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Habilidade> habilidades = new ArrayList<>();

	@OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Experiencia> experiencias = new ArrayList<>();

	@OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Formacao> formacoes = new ArrayList<>();

	@Column(nullable = false)
	@NotNull(message = "A data de nascimento é obrigatória")
	private String dataNascimento;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "contato_id", referencedColumnName = "id")
	private Contato contato;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "endereco_id", referencedColumnName = "id")
	private Endereco endereco;

	private BigDecimal pretensaoSalarial;

	private String disponibilidade; // Imediata, 30 dias, etc.

	private String modalidadePreferida; // Presencial, Remoto, Híbrido

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SituacaoCandidato situacao;

	@Column(nullable = false)
	private Status status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "criado_em", updatable = false)
	private Date criadoEm;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "atualizado_em", nullable = true)
	private Date atualizadoEm;

	public Candidato() {
		this.situacao = SituacaoCandidato.CADASTRADO;
		this.status = Status.ATIVO;
	}

	public Candidato(String rg, String cpf, String nomeCandidato, String dataNascimento,
					 Contato contato, Endereco endereco) {
		this.rg = rg;
		this.cpf = cpf;
		this.nomeCandidato = nomeCandidato;
		this.dataNascimento = dataNascimento;
		this.contato = contato;
		this.endereco = endereco;
		this.situacao = SituacaoCandidato.CADASTRADO;
		this.status = Status.ATIVO;
	}


	@PrePersist
	protected void onCreate() {
		criadoEm = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		atualizadoEm = new Date();
	}

	public List<Vaga> getVagas() {
		return vagas;
	}

	public void setVagas(List<Vaga> vagas) {
		this.vagas = vagas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCriadoEm() {
		return criadoEm;
	}

	public Date getAtualizadoEm() {
		return atualizadoEm;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNomeCandidato() {
		return nomeCandidato;
	}

	public void setNomeCandidato(String nomeCandidato) {
		this.nomeCandidato = nomeCandidato;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public SituacaoCandidato getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoCandidato situacao) {
		this.situacao = situacao;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Habilidade> getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(List<Habilidade> habilidades) {
		this.habilidades = habilidades;
	}

	public List<Experiencia> getExperiencias() {
		return experiencias;
	}

	public void setExperiencias(List<Experiencia> experiencias) {
		this.experiencias = experiencias;
	}

	public List<Formacao> getFormacoes() {
		return formacoes;
	}

	public void setFormacoes(List<Formacao> formacoes) {
		this.formacoes = formacoes;
	}

	public BigDecimal getPretensaoSalarial() {
		return pretensaoSalarial;
	}

	public void setPretensaoSalarial(BigDecimal pretensaoSalarial) {
		this.pretensaoSalarial = pretensaoSalarial;
	}

	public String getDisponibilidade() {
		return disponibilidade;
	}

	public void setDisponibilidade(String disponibilidade) {
		this.disponibilidade = disponibilidade;
	}

	public String getModalidadePreferida() {
		return modalidadePreferida;
	}

	public void setModalidadePreferida(String modalidadePreferida) {
		this.modalidadePreferida = modalidadePreferida;
	}
}
