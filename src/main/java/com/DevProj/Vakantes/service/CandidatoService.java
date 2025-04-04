package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.repository.CandidatoRepository;
import com.DevProj.Vakantes.repository.VagaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CandidatoService {
    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private VagaRepository vagaRepository;

    @Transactional
    public void deletarCandidato(String cpf) {
        Candidato candidato = candidatoRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

        for (Vaga vaga : candidato.getVagas()) {
            vaga.getCandidatos().remove(candidato);
            vagaRepository.save(vaga);
        }

    }
}
