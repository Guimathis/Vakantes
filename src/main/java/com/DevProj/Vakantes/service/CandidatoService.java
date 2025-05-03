package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.util.Status;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.repository.CandidatoRepository;
import com.DevProj.Vakantes.repository.VagaRepository;
import com.DevProj.Vakantes.service.exceptions.DataBindingViolationException;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CandidatoService {
    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private VagaRepository vagaRepository;

    public void cadastrar(Candidato candidato) throws DataBindingViolationException {
        validarCandidato(candidato);
        candidatoRepository.save(candidato);
    }

    public void editar(Candidato candidato) throws DataBindingViolationException {
        validarCandidato(candidato);
        candidatoRepository.save(candidato);
    }
    private void validarCandidato(Candidato candidato) throws DataBindingViolationException {
        // Validar nome do candidato
        if (candidato.getNomeCandidato() == null || candidato.getNomeCandidato().trim().isEmpty()) {
            throw new DataBindingViolationException("O nome do candidato é obrigatório.");
        }

        // Validar CPF
        if (candidato.getCpf() == null || candidato.getCpf().trim().isEmpty()) {
            throw new DataBindingViolationException("O CPF é obrigatório.");
        }

        if (!validarCPF(candidato.getCpf())) {
            throw new DataBindingViolationException("CPF inválido.");
        }

        // Validar RG
        if (candidato.getRg().equals("__.___.___-_") || candidato.getRg().trim().isEmpty()) {
            throw new DataBindingViolationException("O RG é obrigatório.");
        }


        if (candidato.getContato().getEmail() == null || candidato.getContato().getEmail().trim().isEmpty()) {
            throw new DataBindingViolationException("O e-mail de contato é obrigatório.");
        }

        if (!validarEmail(candidato.getContato().getEmail())) {
            throw new DataBindingViolationException("O e-mail de contato informado não é válido.");
        }

        // Validar contato
        if (candidato.getContato() == null) {
            throw new DataBindingViolationException("O contato é obrigatório.");
        }


        if (candidato.getContato().getTelefone().equals("(__) _____-____") || candidato.getContato().getTelefone().trim().isEmpty()) {
            throw new DataBindingViolationException("O telefone de contato é obrigatório.");
        }

        // Validar data de nascimento
        if (candidato.getDataNascimento() == null || candidato.getDataNascimento().trim().isEmpty()) {
            throw new DataBindingViolationException("A data de nascimento é obrigatória.");
        }

        // Validar endereço
        if (candidato.getEndereco() == null) {
            throw new DataBindingViolationException("O endereço é obrigatório.");
        }

        if (candidato.getEndereco().getRua() == null || candidato.getEndereco().getRua().trim().isEmpty()) {
            throw new DataBindingViolationException("A rua é obrigatória.");
        }

        if (candidato.getEndereco().getNumero() == null || candidato.getEndereco().getNumero().trim().isEmpty()) {
            throw new DataBindingViolationException("O número é obrigatório.");
        }

        if (candidato.getEndereco().getBairro() == null || candidato.getEndereco().getBairro().trim().isEmpty()) {
            throw new DataBindingViolationException("O bairro é obrigatório.");
        }

        if (candidato.getEndereco().getCidade() == null || candidato.getEndereco().getCidade().trim().isEmpty()) {
            throw new DataBindingViolationException("A cidade é obrigatória.");
        }

        if (candidato.getEndereco().getEstado() == null || candidato.getEndereco().getEstado().trim().isEmpty()) {
            throw new DataBindingViolationException("O estado é obrigatório.");
        }

        if (candidato.getEndereco().getCep().equals("_____-___")|| candidato.getEndereco().getCep().trim().isEmpty()) {
            throw new DataBindingViolationException("O CEP é obrigatório.");
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

    public Object buscarClientePorId(Long id) {
        return candidatoRepository.findByIdAndStatus(id, Status.ATIVO)
                .orElseThrow(() -> new ObjectNotFoundException("Ocorreu um problema ao buscar o candidato"));
    }

    public Iterable<Candidato> buscarTodos() {
        Iterable<Candidato> todos = candidatoRepository.findAll();
        ArrayList<Candidato> ativos = new ArrayList<>();
        todos.forEach(candidato -> {
            if (candidato.getStatus() != Status.INATIVO) {
                ativos.add(candidato);
            }
        });
        return ativos;
    }

    @Transactional
    public void deletarCandidato(String cpf) {
        Candidato candidato = candidatoRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));
        for (Vaga vaga : candidato.getVagas()) {
            vaga.getCandidatos().remove(candidato);
            vagaRepository.save(vaga);
        }
    }

    public void deletar(Long id) {
        Candidato candidato = candidatoRepository.findByIdAndStatus(id, Status.ATIVO)
                .orElseThrow(() -> new ObjectNotFoundException("Ocorreu um problema ao deletar o candidato"));
        for (Vaga vaga : candidato.getVagas()) {
            vaga.getCandidatos().remove(candidato);
            vagaRepository.save(vaga);
        }
        candidato.setStatus(Status.INATIVO);
        candidatoRepository.save(candidato);
    }
}
