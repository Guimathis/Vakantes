package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public List<Cliente> buscarClientePorResponsavel(Usuario usuario) {
        return clienteRepository.getClientesByUsuarioResponsavel(usuario);
    }

    // Outros métodos como listar, atualizar, deletar...
}