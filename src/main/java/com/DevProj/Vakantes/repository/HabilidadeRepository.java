package com.DevProj.Vakantes.repository;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.candidato.Habilidade;
import com.DevProj.Vakantes.model.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabilidadeRepository extends JpaRepository<Habilidade, Long> {
    List<Habilidade> findAllByCandidatoAndStatus(Candidato candidato, Status status);
    Optional<Habilidade> findByIdAndStatus(Long id, Status status);
    List<Habilidade> findAllByStatus(Status status);
    List<Habilidade> findAllByNomeContainingIgnoreCaseAndStatus(String nome, Status status);
}