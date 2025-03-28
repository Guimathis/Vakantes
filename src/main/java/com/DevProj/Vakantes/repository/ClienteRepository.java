package com.DevProj.Vakantes.repository;

import com.DevProj.Vakantes.model.empresa.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}