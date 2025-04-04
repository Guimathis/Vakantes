package com.DevProj.Vakantes.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.vaga.Vaga;
import org.springframework.data.repository.CrudRepository;

public interface CandidatoRepository extends CrudRepository<Candidato, String> {

	Optional<Candidato> findByRg(String rg);

	Optional<Candidato> findByCpf(String cpf);

	Optional<Candidato> findById(long id);

	Optional<Candidato> findByCpfIn(Collection<String> cpfs);

	List<Candidato> findAllByCpfIn(Collection<String> cpfs);

	
	
	List<Candidato> findByNomeCandidato(String nomeCandidato);
}
