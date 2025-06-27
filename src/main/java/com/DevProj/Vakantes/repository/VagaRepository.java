package com.DevProj.Vakantes.repository;

import java.util.List;
import java.util.Optional;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.util.enums.Status;
import com.DevProj.Vakantes.model.vaga.Vaga;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VagaRepository extends CrudRepository<Vaga, String> {

	Optional<Vaga> findByCodigoAndStatus(long codigo, Status status);

	Iterable<Vaga> findAllByStatus(Status status);

	List<Vaga> findByNome(String nome);

	List<Vaga> findByNomeContainingIgnoreCaseAndStatus(String nome, Status status);

	List<Vaga> findByClienteAndStatus(Cliente cliente, Status status);

	@Query("SELECT DISTINCT v FROM Vaga v JOIN v.candidaturas c WHERE c.candidato = :candidato AND v.status = :status")
	List<Vaga> findByCandidatoAndStatus(@Param("candidato") Candidato candidato, @Param("status") Status status);

	@Query("SELECT DISTINCT v FROM Vaga v " +
           "LEFT JOIN v.candidaturas c " +
           "WHERE v.status = :status " +
           "AND (:clienteId IS NULL OR v.cliente.id = :clienteId) " +
           "AND (:vagaNome IS NULL OR LOWER(v.nome) LIKE LOWER(CONCAT('%', :vagaNome, '%'))) " +
           "AND (:candidatoId IS NULL OR c.candidato.id = :candidatoId)")
	List<Vaga> findByFilters(
	    @Param("status") Status status,
	    @Param("clienteId") Long clienteId,
	    @Param("vagaNome") String vagaNome,
	    @Param("candidatoId") Long candidatoId
	);
}
