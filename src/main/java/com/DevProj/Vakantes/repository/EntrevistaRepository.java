package com.DevProj.Vakantes.repository;

import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.entrevista.Entrevista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntrevistaRepository extends JpaRepository<Entrevista, Long> {
}

