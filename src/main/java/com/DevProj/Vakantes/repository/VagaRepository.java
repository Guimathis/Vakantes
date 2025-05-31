package com.DevProj.Vakantes.repository;

import java.util.List;
import java.util.Optional;

import com.DevProj.Vakantes.model.util.enums.Status;
import com.DevProj.Vakantes.model.vaga.Vaga;
import org.springframework.data.repository.CrudRepository;

public interface VagaRepository extends CrudRepository<Vaga, String> {

	Optional<Vaga> findByCodigoAndStatus(long codigo, Status status);

	Iterable<Vaga> findAllByStatus(Status status);

	List<Vaga> findByNome(String nome);
}
