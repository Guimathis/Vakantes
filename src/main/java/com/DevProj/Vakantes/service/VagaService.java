package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.util.enums.Status;
import com.DevProj.Vakantes.model.vaga.Candidatura;
import com.DevProj.Vakantes.model.vaga.enums.StatusProcesso;
import com.DevProj.Vakantes.model.vaga.Requisito;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.model.vaga.VagaDTO;
import com.DevProj.Vakantes.repository.RequisitoRepository;
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
    private CandidatoService candidatoService;

    @Autowired
    CandidaturaService candidaturaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmailService emailService;


    public Iterable<Vaga> buscarTodas() {
        return vagaRepository.findAllByStatus(Status.ATIVO);
    }

    public Vaga buscarVagaPorID(Long id) {
        return vagaRepository.findByCodigoAndStatus(id, Status.ATIVO)
                .orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));
    }

    public void salvar(Vaga vaga) {
        vagaRepository.save(vaga);
    }

    public void inscreverCandidatos(Long vagaId, List<String> cpfs) {
        Vaga vaga = buscarVagaPorID(vagaId);
        List<Candidato> candidatos = candidatoService.buscarTodosPorCpfs(cpfs);
        candidaturaService.criarCandidaturas(vaga, candidatos);
        atualizarStatusVaga(vaga);
        candidatoService.saveAll(candidatos);
        salvar(vaga);
    }

    private void atualizarStatusVaga(Vaga vaga) {
        if (vaga.getStatusProcesso() == StatusProcesso.ABERTA) {
            vaga.setStatusProcesso(StatusProcesso.SELECAO);
        }
    }

    public Long cadastrarVaga(VagaDTO vagaDTO) {
        Vaga vaga = new Vaga(vagaDTO, clienteService.buscarClientePorId(vagaDTO.getIdCliente()));

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
        List<Candidatura> candidaturasByVagaId = candidaturaService.findCandidaturasByVagaId(vaga.getCodigo());
        vaga.getCandidaturas().addAll(candidaturasByVagaId);
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

    public void removerCandidatura(long codigo, long id) {
        Vaga vaga = vagaRepository.findByCodigoAndStatus(codigo, Status.ATIVO)
                .orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));
        candidaturaService.deleteCandidatura(codigo, id);
        if (vaga.getCandidaturas().isEmpty()) {
            vaga.setStatusProcesso(StatusProcesso.ABERTA);
            salvar(vaga);
        }
    }

    public void selecionarCandidato(Long vagaId, Long candidaturaId) {
        Vaga vaga = vagaRepository.findByCodigoAndStatus(vagaId, Status.ATIVO)
                .orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));

        Candidatura candidaturaSelecionada = candidaturaService.buscaCandidaturaById(candidaturaId);

        // Atualizar status do candidato selecionado
        candidaturaSelecionada.setStatus(Candidatura.StatusCandidatura.SELECIONADO);
        candidaturaService.salvar(candidaturaSelecionada);

        // Rejeitar outros candidatos
        vaga.getCandidaturas().stream()
                .filter(c -> !c.getId().equals(candidaturaId))
                .forEach(c -> {
                    c.setStatus(Candidatura.StatusCandidatura.REJEITADO);
                    candidaturaService.salvar(c);
                });

        // Finalizar vaga
        vaga.setStatusProcesso(StatusProcesso.FINALIZADA);
        vagaRepository.save(vaga);

        // Opcionalmente enviar notificações
        emailService.notificarCandidatoSelecionado(candidaturaSelecionada);
    }
}
