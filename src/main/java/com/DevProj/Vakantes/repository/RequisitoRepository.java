package com.DevProj.Vakantes.repository;

import com.DevProj.Vakantes.model.util.enums.Status;
import com.DevProj.Vakantes.model.vaga.Requisito;
import com.DevProj.Vakantes.model.vaga.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequisitoRepository extends JpaRepository<Requisito, Long> {
    List<Requisito> findAllByVagaAndStatus(Vaga vaga, Status status);
    Optional<Requisito> findByIdAndStatus(Long id, Status status);
    List<Requisito> findAllByStatus(Status status);
}