package com.DevProj.Vakantes.repository;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.candidato.Formacao;
import com.DevProj.Vakantes.model.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormacaoRepository extends JpaRepository<Formacao, Long> {
    List<Formacao> findAllByCandidatoAndStatus(Candidato candidato, Status status);
    Optional<Formacao> findByIdAndStatus(Long id, Status status);
    List<Formacao> findAllByStatus(Status status);
    List<Formacao> findAllByCursoContainingIgnoreCaseAndStatus(String curso, Status status);
    List<Formacao> findAllByInstituicaoContainingIgnoreCaseAndStatus(String instituicao, Status status);
    List<Formacao> findAllByNivelAndStatus(String nivel, Status status);
}