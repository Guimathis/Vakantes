package com.DevProj.Vakantes.repository;

import java.util.List;

import com.DevProj.Vakantes.model.vaga.Vaga;
import org.springframework.data.repository.CrudRepository;

public interface VagaRepository extends CrudRepository<Vaga, String> {

	Vaga findByCodigo(long codigo);

	List<Vaga> findByNome(String nome);
}
