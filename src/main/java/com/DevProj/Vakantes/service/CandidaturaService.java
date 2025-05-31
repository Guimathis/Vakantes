package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.vaga.Candidatura;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.repository.CandidaturaRepository;
import com.DevProj.Vakantes.service.exceptions.DataBindingViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidaturaService {
    @Autowired
    private CandidaturaRepository candidaturaRepository;

    public Candidatura findCandidaturaById(Long id) {
        return candidaturaRepository.findById(id).orElse(null);
    }

    public List<Candidatura> findAllCandidaturas() {
        return candidaturaRepository.findAll();
    }

    public List<Candidatura> findCandidaturasByVagaId(Long vagaId) {
        return candidaturaRepository.findByVagaCodigo(vagaId);
    }

    public List<Candidato> buscarCandidatosPorVaga(Long vagaCodigo) {
        return candidaturaRepository.findByVagaCodigo(vagaCodigo)
                .stream()
                .map(Candidatura::getCandidato)
                .toList();
    }

    public List<Vaga> buscarVagasPorCandidato(String cpf) {
        return candidaturaRepository.findByCandidatoCpf(cpf)
                .stream()
                .map(Candidatura::getVaga).toList();
    }

    public void criarCandidaturas(Vaga vaga, List<Candidato> candidatos) {
        validarCandidaturas(vaga, candidatos);

        candidatos.forEach(candidato -> {
            Candidatura novaCandidatura = new Candidatura(vaga, candidato);
            candidato.adicionarCandidatura(novaCandidatura);
            vaga.adicionarCandidatura(novaCandidatura);
            candidaturaRepository.save(novaCandidatura);
        });
    }

    private void validarCandidaturas(Vaga vaga, List<Candidato> candidatos) {
        List<String> candidatosDuplicados = candidatos.stream()
                .filter(c -> vaga.getCandidatos().contains(c))
                .map(Candidato::getNomeCandidato)
                .toList();

        if (!candidatosDuplicados.isEmpty()) {
            throw new DataBindingViolationException(
                    "Candidatos " + String.join(", ", candidatosDuplicados) + " já estão inscritos nesta vaga.");
        }
    }

    public void deleteCandidatura(long codigo, long id) {
        Candidatura c = candidaturaRepository.findCandidaturaByVagaCodigoAndCandidatoId(codigo, id);
        if (c == null) {
            throw new DataBindingViolationException("Candidatura não encontrada");
        }
        candidaturaRepository.delete(c);
    }

}
