package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.model.util.Status;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.repository.ClienteRepository;
import com.DevProj.Vakantes.repository.VagaRepository;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Iterable<Cliente> buscarTodos(){
        return clienteRepository.findAllByStatus(Status.ATIVO);
    }

    public Cliente buscarClientePorIdAndStatus(Long id, Status status) {
        return clienteRepository.findById(id).orElse(null);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAllByStatus(Status.ATIVO);
    }

    public List<Cliente> buscarClientePorResponsavel(Usuario usuario) {
        return clienteRepository.getClientesByUsuarioResponsavelAndStatus(usuario, Status.ATIVO);
    }

    public void deletarCliente(Long id) {
        Cliente cliente = clienteRepository.findByIdAndStatus(id, Status.ATIVO)
                .orElseThrow(() -> new ObjectNotFoundException("Ocorreu um problema ao deletar o candidato"));
        for (Vaga vaga : cliente.getVagas()) {
            vaga.setStatus(Status.INATIVO);
            vagaRepository.save(vaga);
        }

        cliente.setStatus(Status.INATIVO);
        clienteRepository.save(cliente);
    }
}
