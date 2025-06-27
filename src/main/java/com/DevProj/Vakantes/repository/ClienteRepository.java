package com.DevProj.Vakantes.repository;

import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.empresa.enums.TipoPessoa;
import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.model.util.enums.Status;
import com.DevProj.Vakantes.model.vaga.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> getClientesByUsuarioResponsavel(Usuario usuarioResponsavel);

    Optional<Cliente> findByIdAndStatus(long id, Status status);

    List<Cliente> findAllByStatus(Status status);

    List<Cliente> getClientesByUsuarioResponsavelAndStatus(Usuario usuario, Status status);

    @Query("SELECT DISTINCT c FROM Cliente c " +
            "WHERE c.status = :status " +
            "AND (:nomeCliente IS NULL OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :nomeCliente, '%'))) "+
    "AND (:tipoPessoa IS NULL OR c.tipoPessoa = :tipoPessoa)")
    List<Cliente> findByFilters(
            @Param("status") Status status,
            @Param("nomeCliente") String nomeCliente,
            @Param("tipoPessoa") TipoPessoa tipoPessoa
    );

}
