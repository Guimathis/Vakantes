package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.util.enums.Status;
import com.DevProj.Vakantes.model.vaga.Candidatura;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.repository.CandidatoRepository;
import com.DevProj.Vakantes.service.exceptions.DataBindingViolationException;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class CandidatoService {
    @Autowired
    private CandidatoRepository candidatoRepository;

//    @Autowired
//    private VagaService vagaService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    CandidaturaService candidaturaService;

    public void salvar(Candidato candidato) throws DataBindingViolationException {
        validarCandidato(candidato);
        updateHabForExp(candidato);
        candidatoRepository.save(candidato);
    }

    public void cadastrar(Candidato candidato) throws DataBindingViolationException {
        validarCandidato(candidato);
        updateHabForExp(candidato);
        candidatoRepository.save(candidato);
    }

    public void editar(Candidato candidato) throws DataBindingViolationException {
        validarCandidato(candidato);
        updateHabForExp(candidato);
        candidatoRepository.save(candidato);
    }

    public Candidato buscarCandidatoPorId(String id) {
        return candidatoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Candidato não encontrado"));
    }

    public List<Candidato> buscarTodosPorCpfs(List<String> cpfs) {
        return candidatoRepository.findAllByCpfIn(cpfs);
    }

    private void updateHabForExp(Candidato candidato) {
        if(candidato.getHabilidades() != null) {
            IntStream.range(0, candidato.getHabilidades().size()).forEach(i -> candidato.getHabilidades().get(i).setCandidato(candidato));
        }
        if(candidato.getExperiencias() != null) {
            IntStream.range(0, candidato.getExperiencias().size()).forEach(i -> candidato.getExperiencias().get(i).setCandidato(candidato));
        }
        if(candidato.getFormacoes() != null) {
            IntStream.range(0, candidato.getFormacoes().size()).forEach(i -> candidato.getFormacoes().get(i).setCandidato(candidato));
        }
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

        if (!validationService.isCPFValido(candidato.getCpf())) {
            throw new DataBindingViolationException("CPF inválido.");
        }

        // Validar RG
        if (candidato.getRg().equals("__.___.___-_") || candidato.getRg().trim().isEmpty()) {
            throw new DataBindingViolationException("O RG é obrigatório.");
        }


        if (candidato.getContato().getEmail() == null || candidato.getContato().getEmail().trim().isEmpty()) {
            throw new DataBindingViolationException("O e-mail de contato é obrigatório.");
        }

        if (!validationService.isEmailValido(candidato.getContato().getEmail())) {
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


    public Object buscarCandidatoPorId(Long id) {
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

    public void deletar(Long id) {
        Candidato candidato = candidatoRepository.findByIdAndStatus(id, Status.ATIVO)
                .orElseThrow(() -> new ObjectNotFoundException("Ocorreu um problema ao deletar o candidato"));
        candidato.getCandidaturas().forEach(candidatura -> {
            candidaturaService.deleteCandidatura(candidatura.getVaga().getCodigo(), candidato.getId());
        });

        candidato.setStatus(Status.INATIVO);
        candidatoRepository.save(candidato);
    }

    public void saveAll(List<Candidato> candidatos) {
        candidatoRepository.saveAll(candidatos);
    }
}
