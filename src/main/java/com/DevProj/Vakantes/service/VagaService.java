package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.model.vaga.VagaDTO;
import com.DevProj.Vakantes.repository.CandidatoRepository;
import com.DevProj.Vakantes.repository.ClienteRepository;
import com.DevProj.Vakantes.repository.VagaRepository;
import com.DevProj.Vakantes.service.exceptions.DataBindingViolationException;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    public void inscreverCandidatos(Long vagaId, List<String> cpfs) {
        Vaga vaga = vagaRepository.findByCodigo(vagaId)
                .orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));

        List<Candidato> candidatos = candidatoRepository.findAllByCpfIn(cpfs);
        StringBuilder mensagemErro = new StringBuilder();

        for (Candidato candidato : candidatos) {
            if (vaga.getCandidatos().contains(candidato)) {
                mensagemErro.append("Candidato ").append(candidato.getNomeCandidato()).append(" já está cadastrado na vaga. ");
            } else {
                candidato.getVagas().add(vaga);
                vaga.getCandidatos().add(candidato);
            }
        }

        if (!mensagemErro.isEmpty()) {
            throw new DataBindingViolationException(mensagemErro.toString());
        }

        candidatoRepository.saveAll(candidatos);
        vagaRepository.save(vaga);
    }

public void adicionarCandidatoAVaga(long codigo, Candidato candidato) {
    Vaga vaga = vagaRepository.findByCodigo(codigo).orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));

    // Verifica se o candidato já está cadastrado na vaga
    if (vaga.getCandidatos().stream().anyMatch(c -> c.getCpf().equals(candidato.getCpf()) || c.getRg().equals(candidato.getRg()))) {
        throw new DataBindingViolationException("Candidato já cadastrado na vaga.");
    }

    candidato.getVagas().add(vaga);
    vaga.getCandidatos().add(candidato);
    candidatoRepository.save(candidato);
    vagaRepository.save(vaga);
}

    public Long cadastrarVaga(VagaDTO vagaDTO) {
        Vaga vaga = new Vaga(vagaDTO, clienteRepository.findById(vagaDTO.getIdCliente()).get());
        vagaRepository.save(vaga);
        return vaga.getCodigo();
    }
}
