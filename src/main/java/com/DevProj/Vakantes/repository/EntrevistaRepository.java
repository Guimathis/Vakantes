package com.DevProj.Vakantes.repository;

import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.entrevista.Entrevista;
import com.DevProj.Vakantes.model.vaga.Candidatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EntrevistaRepository extends JpaRepository<Entrevista, Long> {

    @Query("SELECT e FROM Entrevista e " +
           "WHERE (:clienteId IS NULL OR e.candidatura.vaga.cliente.id = :clienteId) " +
           "AND (:vagaId IS NULL OR e.candidatura.vaga.codigo = :vagaId) " +
           "AND (:candidatoId IS NULL OR e.candidatura.candidato.id = :candidatoId) " +
           "AND (:status IS NULL OR e.candidatura.status = :status) ")
    List<Entrevista> findByFilters(
        @Param("clienteId") Long clienteId,
        @Param("vagaId") Long vagaId,
        @Param("candidatoId") Long candidatoId,
        @Param("status") Candidatura.StatusCandidatura status
    );
}
