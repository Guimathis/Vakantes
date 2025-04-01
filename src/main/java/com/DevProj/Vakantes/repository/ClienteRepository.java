package com.DevProj.Vakantes.repository;

import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> getClientesByUsuarioResponsavel(Usuario usuarioResponsavel);
}