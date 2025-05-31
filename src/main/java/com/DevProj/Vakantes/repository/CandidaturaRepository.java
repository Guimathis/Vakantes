package com.DevProj.Vakantes.repository;

import com.DevProj.Vakantes.model.vaga.Candidatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {
    List<Candidatura> findByVagaCodigo(Long vagaCodigo);

    List<Candidatura> findByCandidatoCpf(String cpf);

    Candidatura findCandidaturaByVagaCodigoAndCandidatoId(long codigo, long id);
}