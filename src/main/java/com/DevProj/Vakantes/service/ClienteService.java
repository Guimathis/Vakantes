package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.model.util.Status;
import com.DevProj.Vakantes.model.util.enums.TipoPessoa;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.repository.ClienteRepository;
import com.DevProj.Vakantes.repository.VagaRepository;
import com.DevProj.Vakantes.service.exceptions.DataBindingViolationException;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ClienteService {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

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
            if (!validarCPF(cliente.getDocumento())) {
                throw new DataBindingViolationException("CPF inválido.");
            }
        } else if (cliente.getTipoPessoa() == TipoPessoa.JURIDICA) {
            if (!validarCNPJ(cliente.getDocumento())) {
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

        if (!validarEmail(cliente.getContato().getEmail())) {
            throw new DataBindingViolationException("O e-mail de contato informado não é válido.");
        }

        if (cliente.getContato().getTelefone() == null || cliente.getContato().getTelefone().trim().isEmpty()) {
            throw new DataBindingViolationException("O telefone de contato é obrigatório.");
        }
    }

    private boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validarCPF(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais
        boolean todosDigitosIguais = true;
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                todosDigitosIguais = false;
                break;
            }
        }
        if (todosDigitosIguais) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int resto = soma % 11;
        int dv1 = (resto < 2) ? 0 : 11 - resto;

        // Verifica o primeiro dígito verificador
        if (dv1 != (cpf.charAt(9) - '0')) {
            return false;
        }

        // Calcula o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        resto = soma % 11;
        int dv2 = (resto < 2) ? 0 : 11 - resto;

        // Verifica o segundo dígito verificador
        return dv2 == (cpf.charAt(10) - '0');
    }

    private boolean validarCNPJ(String cnpj) {
        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // Verifica se tem 14 dígitos
        if (cnpj.length() != 14) {
            return false;
        }

        // Verifica se todos os dígitos são iguais
        boolean todosDigitosIguais = true;
        for (int i = 1; i < cnpj.length(); i++) {
            if (cnpj.charAt(i) != cnpj.charAt(0)) {
                todosDigitosIguais = false;
                break;
            }
        }
        if (todosDigitosIguais) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int[] multiplicadores1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += (cnpj.charAt(i) - '0') * multiplicadores1[i];
        }
        int resto = soma % 11;
        int dv1 = (resto < 2) ? 0 : 11 - resto;

        // Verifica o primeiro dígito verificador
        if (dv1 != (cnpj.charAt(12) - '0')) {
            return false;
        }

        // Calcula o segundo dígito verificador
        int[] multiplicadores2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        soma = 0;
        for (int i = 0; i < 13; i++) {
            soma += (cnpj.charAt(i) - '0') * multiplicadores2[i];
        }
        resto = soma % 11;
        int dv2 = (resto < 2) ? 0 : 11 - resto;

        // Verifica o segundo dígito verificador
        return dv2 == (cnpj.charAt(13) - '0');
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
