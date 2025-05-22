package com.DevProj.Vakantes.repository;

import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.model.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> getClientesByUsuarioResponsavel(Usuario usuarioResponsavel);

    Optional<Cliente> findByIdAndStatus(long id, Status status);

    List<Cliente> findAllByStatus(Status status);

    List<Cliente> getClientesByUsuarioResponsavelAndStatus(Usuario usuario, Status status);
}