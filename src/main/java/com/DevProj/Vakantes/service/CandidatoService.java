package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.util.Status;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.repository.CandidatoRepository;
import com.DevProj.Vakantes.repository.VagaRepository;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class CandidatoService {
    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private VagaRepository vagaRepository;

    public void cadastrar(Candidato candidato) {
        candidatoRepository.save(candidato);
    }

    public void editar(Candidato candidato) {
        candidatoRepository.save(candidato);
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
