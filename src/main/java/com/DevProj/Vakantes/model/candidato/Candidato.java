package com.DevProj.Vakantes.model.candidato;

import com.DevProj.Vakantes.model.vaga.Vaga;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "candidato", schema = "candidato")
public class Candidato {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Candidato_seq")
	@SequenceGenerator(name = "Candidato_seq", sequenceName = "canditado.Candidato_seq", allocationSize = 1)
	private long id;
	
	@Column(unique = true)
	private String rg;
	
	@NotEmpty
	private String nomeCandidato;
	
	@NotEmpty
	private String email;
	
	@ManyToOne
	private Vaga vaga;

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getNomeCandidato() {
		return nomeCandidato;
	}

	public void setNomeCandidato(String nomeCandidato) {
		this.nomeCandidato = nomeCandidato;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Vaga getVaga() {
		return vaga;
	}

	public void setVaga(Vaga vaga) {
		this.vaga = vaga;
	}
	
	

}
