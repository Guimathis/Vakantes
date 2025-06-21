package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.entrevista.Entrevista;
import com.DevProj.Vakantes.model.entrevista.enums.StatusEntrevista;
import com.DevProj.Vakantes.model.entrevista.enums.StatusNotificacaoEmail;
import com.DevProj.Vakantes.model.vaga.Candidatura;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.repository.EntrevistaRepository;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Service
public class EntrevistaService {

    @Autowired
    private EntrevistaRepository entrevistaRepository;

    @Autowired
    private VagaService vagaService;

    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CandidaturaService candidaturaService;

    public void salvarEntrevista(Long candidaturaId, String local, String dataHora, String observacoes, RedirectAttributes redirectAttributes) {
        Candidatura candidatura = candidaturaService.buscaCandidaturaById(candidaturaId);

        if(candidaturaPossuiEntrevistaAgendada(candidatura)){
            throw new RuntimeException("O candidato já possui uma entrevista agendada para esta vaga.");
        }

        Entrevista entrevista = new Entrevista(local, observacoes, LocalDateTime.parse(dataHora,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")), candidatura, LocalDateTime.now(), StatusNotificacaoEmail.PENDENTE);
        candidatura.setEntrevista(entrevista);

        boolean emailEnviado = false;
        try {
            emailService.enviarEmailAgendamentoEntrevista(
                    candidatura.getCandidato(),
                    candidatura.getVaga(),
                    dataHora,
                    local,
                    observacoes
            );
            emailEnviado = true;
        } catch (RuntimeException e) {
            entrevista.setStatusNotificacaoEmail(StatusNotificacaoEmail.ERRO);
            redirectAttributes.addFlashAttribute("mensagem_erro_email", e.getMessage());
        }

        if (emailEnviado) {
            entrevista.setStatusNotificacaoEmail(StatusNotificacaoEmail.ENVIADO);
        }

        entrevistaRepository.save(entrevista);
    }

    public Entrevista buscarPorId(Long id) {
        return entrevistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrevista não encontrada com o ID: " + id));
    }

    public Collection<Entrevista> buscarTodas() {
        return entrevistaRepository.findAll();
    }

    public boolean candidaturaPossuiEntrevistaAgendada(Candidatura candidatura) {
        if (candidatura == null || candidatura.getEntrevista() == null) {
            return false;
        }
        Entrevista entrevista = candidatura.getEntrevista();
        return entrevista.getStatusEntrevista() == StatusEntrevista.AGENDADA;
    }

    public void editarEntrevista(Long id, String local, String dataHora, String observacoes) {
        Entrevista entrevista = buscarPorId(id);
        entrevista.setLocal(local);
        entrevista.setDataHora(LocalDateTime.parse(dataHora, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        entrevista.setObservacoes(observacoes);
        entrevistaRepository.save(entrevista);
    }

}
