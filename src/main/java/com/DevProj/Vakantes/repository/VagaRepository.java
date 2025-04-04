package com.DevProj.Vakantes.repository;

import java.util.List;
import java.util.Optional;

import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.model.vaga.Vaga;
import org.springframework.data.repository.CrudRepository;

public interface VagaRepository extends CrudRepository<Vaga, String> {

	Optional<Vaga> findByCodigo(long codigo);

	List<Vaga> findByNome(String nome);
}
