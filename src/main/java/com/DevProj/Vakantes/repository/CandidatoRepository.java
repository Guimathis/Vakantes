package com.DevProj.Vakantes.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.util.enums.Status;
import com.DevProj.Vakantes.model.vaga.Vaga;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CandidatoRepository extends CrudRepository<Candidato, String> {

	Optional<Candidato> findByRg(String rg);

	Optional<Candidato> findByCpf(String cpf);

	Optional<Candidato> findByIdAndStatus(long id, Status status);

	Optional<Candidato> findByCpfIn(Collection<String> cpfs);

	List<Candidato> findAllByCpfIn(Collection<String> cpfs);

	List<Candidato> findByNomeCandidato(String nomeCandidato);

	Iterable<Candidato> findAllByStatus(Status status);

@Query("SELECT DISTINCT c FROM Candidato c " +
		"WHERE c.status = :status " +
		"AND (:candidatoNome IS NULL OR LOWER(c.nomeCandidato) LIKE LOWER(CONCAT('%', :candidatoNome, '%')))" +
		"AND (:cidade IS NULL OR c.endereco.cidade = :cidade)")
List<Candidato> findByFilters(
		@Param("status") Status status,
		@Param("candidatoNome") String candidatoNome,
		@Param("cidade") String cidade
);

}
