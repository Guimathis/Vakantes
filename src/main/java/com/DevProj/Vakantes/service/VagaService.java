package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.util.Status;
import com.DevProj.Vakantes.model.vaga.StatusProcesso;
import com.DevProj.Vakantes.model.vaga.Requisito;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.model.vaga.VagaDTO;
import com.DevProj.Vakantes.repository.CandidatoRepository;
import com.DevProj.Vakantes.repository.ClienteRepository;
import com.DevProj.Vakantes.repository.VagaRepository;
import com.DevProj.Vakantes.service.exceptions.DataBindingViolationException;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    public Iterable<Vaga> buscarTodas() {
        return vagaRepository.findAllByStatus(Status.ATIVO);
    }

    public void inscreverCandidatos(Long vagaId, List<String> cpfs) {
        if (cpfs.get(0).equals("cpfs")) {
            throw new DataBindingViolationException("Nenhum Candidato selecionado");
        }
        Vaga vaga = vagaRepository.findByCodigoAndStatus(vagaId, Status.ATIVO)
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
        if(vaga.getStatusProcesso() == StatusProcesso.ABERTA){
            vaga.setStatusProcesso(StatusProcesso.SELECAO);
        }
        candidatoRepository.saveAll(candidatos);
        vagaRepository.save(vaga);
    }

    public void adicionarCandidatoAVaga(long codigo, Candidato candidato) {
        Vaga vaga = vagaRepository.findByCodigoAndStatus(codigo, Status.ATIVO).orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));

        // Verifica se o candidato já está cadastrado na vaga
        if (vaga.getCandidatos().stream().anyMatch(c -> c.getCpf().equals(candidato.getCpf()) || c.getRg().equals(candidato.getRg()))) {
            throw new DataBindingViolationException("Candidato já cadastrado na vaga.");
        }
        if(vaga.getStatusProcesso() == StatusProcesso.ABERTA){
            vaga.setStatusProcesso(StatusProcesso.SELECAO);
        }
        candidato.getVagas().add(vaga);
        vaga.getCandidatos().add(candidato);
        candidatoRepository.save(candidato);
        vagaRepository.save(vaga);
    }

    public Long cadastrarVaga(VagaDTO vagaDTO) {
        Vaga vaga = new Vaga(vagaDTO, clienteRepository.findById(vagaDTO.getIdCliente()).get());

        // Limpar a lista de requisitos existente
        vaga.getRequisitos().clear();

        // Copiar os requisitos do DTO para a entidade
        if (vagaDTO.getRequisitos() != null && !vagaDTO.getRequisitos().isEmpty()) {
            for (Requisito requisito : vagaDTO.getRequisitos()) {
                // Criar um novo requisito e copiar os dados
                Requisito novoRequisito = new Requisito();
                novoRequisito.setNome(requisito.getNome());
                novoRequisito.setNivelMinimo(requisito.getNivelMinimo());
                novoRequisito.setObrigatorio(requisito.getObrigatorio());
                novoRequisito.setVaga(vaga);

                // Adicionar o novo requisito à lista da vaga
                vaga.getRequisitos().add(novoRequisito);
            }
        }

        vagaRepository.save(vaga);
        return vaga.getCodigo();
    }

    public void deletarVaga(Long codigo) {
        Vaga vaga = vagaRepository.findByCodigoAndStatus(codigo, Status.ATIVO).orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));
        try {
            vaga.setStatus(Status.INATIVO);
            vagaRepository.save(vaga);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não foi possível deletar a vaga " + vaga.getNome() + ", pois há candidatos vinculados a ela.");
        }
    }
}
