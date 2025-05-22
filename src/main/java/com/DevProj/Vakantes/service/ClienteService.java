package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.model.util.enums.Status;
import com.DevProj.Vakantes.model.empresa.enums.TipoPessoa;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.repository.ClienteRepository;
import com.DevProj.Vakantes.repository.VagaRepository;
import com.DevProj.Vakantes.service.exceptions.DataBindingViolationException;
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

    @Autowired
    private ValidationService validationService;

    public void salvarCliente(Cliente cliente) throws DataBindingViolationException {
        validarCliente(cliente);
        clienteRepository.save(cliente);
    }

    private void validarCliente(Cliente cliente) throws DataBindingViolationException {
        // Validar nome
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new DataBindingViolationException("O nome do cliente é obrigatório.");
        }

        // Validar tipo de pessoa
        if (cliente.getTipoPessoa() == null) {
            throw new DataBindingViolationException("O tipo de pessoa é obrigatório.");
        }

        // Validar documento (CPF ou CNPJ)
        if (cliente.getDocumento() == null || cliente.getDocumento().trim().isEmpty()) {
            throw new DataBindingViolationException("O documento (CPF/CNPJ) é obrigatório.");
        }

        // Validar formato do documento baseado no tipo de pessoa
        if (cliente.getTipoPessoa() == TipoPessoa.FISICA) {
            if (!validationService.isCPFValido(cliente.getDocumento())) {
                throw new DataBindingViolationException("CPF inválido.");
            }
        } else if (cliente.getTipoPessoa() == TipoPessoa.JURIDICA) {
            if (!validationService.isCNPJValido(cliente.getDocumento())) {
                throw new DataBindingViolationException("CNPJ inválido.");
            }
        }

        // Validar endereço
        if (cliente.getEndereco() == null) {
            throw new DataBindingViolationException("O endereço é obrigatório.");
        }

        if (cliente.getEndereco().getRua() == null || cliente.getEndereco().getRua().trim().isEmpty()) {
            throw new DataBindingViolationException("A rua é obrigatória.");
        }

        if (cliente.getEndereco().getNumero() == null || cliente.getEndereco().getNumero().trim().isEmpty()) {
            throw new DataBindingViolationException("O número é obrigatório.");
        }

        if (cliente.getEndereco().getBairro() == null || cliente.getEndereco().getBairro().trim().isEmpty()) {
            throw new DataBindingViolationException("O bairro é obrigatório.");
        }

        if (cliente.getEndereco().getCidade() == null || cliente.getEndereco().getCidade().trim().isEmpty()) {
            throw new DataBindingViolationException("A cidade é obrigatória.");
        }

        if (cliente.getEndereco().getEstado() == null || cliente.getEndereco().getEstado().trim().isEmpty()) {
            throw new DataBindingViolationException("O estado é obrigatório.");
        }

        if (cliente.getEndereco().getCep() == null || cliente.getEndereco().getCep().trim().isEmpty()) {
            throw new DataBindingViolationException("O CEP é obrigatório.");
        }

        // Validar contato
        if (cliente.getContato() == null) {
            throw new DataBindingViolationException("O contato é obrigatório.");
        }

        if (cliente.getContato().getEmail() == null || cliente.getContato().getEmail().trim().isEmpty()) {
            throw new DataBindingViolationException("O e-mail de contato é obrigatório.");
        }

        if (!validationService.isEmailValido(cliente.getContato().getEmail())) {
            throw new DataBindingViolationException("O e-mail de contato informado não é válido.");
        }

        if (cliente.getContato().getTelefone() == null || cliente.getContato().getTelefone().trim().isEmpty()) {
            throw new DataBindingViolationException("O telefone de contato é obrigatório.");
        }
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
