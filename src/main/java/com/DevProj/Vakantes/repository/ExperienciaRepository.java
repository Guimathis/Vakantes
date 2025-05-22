package com.DevProj.Vakantes.repository;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.candidato.Experiencia;
import com.DevProj.Vakantes.model.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperienciaRepository extends JpaRepository<Experiencia, Long> {
    List<Experiencia> findAllByCandidatoAndStatus(Candidato candidato, Status status);
    Optional<Experiencia> findByIdAndStatus(Long id, Status status);
    List<Experiencia> findAllByStatus(Status status);
    List<Experiencia> findAllByCargoContainingIgnoreCaseAndStatus(String cargo, Status status);
    List<Experiencia> findAllByEmpresaContainingIgnoreCaseAndStatus(String empresa, Status status);
}