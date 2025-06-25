package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.comunicacao.Comunicacao;
import com.DevProj.Vakantes.model.entrevista.Entrevista;
import com.DevProj.Vakantes.model.entrevista.enums.StatusEntrevista;
import com.DevProj.Vakantes.model.entrevista.enums.StatusNotificacaoEmail;
import com.DevProj.Vakantes.model.vaga.Candidatura;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.model.vaga.enums.StatusProcesso;
import com.DevProj.Vakantes.repository.ComunicacaoRepository;
import com.DevProj.Vakantes.repository.EntrevistaRepository;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class EntrevistaService {

    @Autowired
    private EntrevistaRepository entrevistaRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CandidaturaService candidaturaService;

    @Autowired
    private ComunicacaoService comunicacaoService;
    @Autowired
    private ComunicacaoRepository comunicacaoRepository;
    @Autowired
    private VagaService vagaService;

    public void salvarEntrevista(Long candidaturaId, String local, String dataHora, String observacoes, RedirectAttributes redirectAttributes) {
        Candidatura candidatura = candidaturaService.buscaCandidaturaById(candidaturaId);

        if(candidaturaPossuiEntrevistaAgendada(candidatura)){
            throw new RuntimeException("O candidato já possui uma entrevista agendada para esta vaga.");
        }

        Comunicacao comunicacao = comunicacaoService.novaComunicacao(candidatura.getCandidato().getContato().getEmail(), observacoes);

        if (comunicacao == null) {
            throw new RuntimeException("Erro ao criar comunicação para a entrevista.");
        }

        Entrevista entrevista = new Entrevista(local, observacoes, LocalDateTime.parse(dataHora,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")), candidatura, new ArrayList<>(List.of(comunicacao)));
        candidatura.setEntrevista(entrevista);

        comunicacao.setEntrevista(entrevista);

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
            redirectAttributes.addFlashAttribute("mensagem_erro_email", e.getMessage());
        }

        if (emailEnviado) {
            comunicacao.setStatusEnvio(StatusNotificacaoEmail.ENVIADO);
        }
        comunicacaoService.salvarComunicacao(comunicacao);
        candidaturaService.salvar(candidatura);
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
        LocalDateTime novaDataHora = LocalDateTime.parse(dataHora, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        entrevista.setDataHora(novaDataHora);
        entrevista.setObservacoes(observacoes);
        entrevistaRepository.save(entrevista);
        Candidato candidato = entrevista.getCandidatura().getCandidato();
        Vaga vaga = entrevista.getCandidatura().getVaga();
        String mensagem = "Os dados da sua entrevista foram alterados!\n\n" +
                "Novos dados:\n" +
                "Data e horário: " + novaDataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n" +
                "Local: " + local + "\n" +
                (observacoes != null && !observacoes.isEmpty() ? "Observações: " + observacoes + "\n" : "");
        comunicacaoService.salvarEEnviarComunicacao(entrevista, candidato.getContato().getEmail(), mensagem);
        emailService.enviarEmailAlteracaoEntrevista(candidato, vaga, novaDataHora, local, observacoes);
    }

    public void comunicarCandidatoPorEmail(String email, String mensagem) {
        try {
            String assunto = "Vakantes - Mensagem do Processo Seletivo";
            emailService.enviarEmailSimples(email, assunto, mensagem);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage(), e);
        }
    }

    public void aprovarCandidaturaNaEntrevista(Long entrevistaId) {
        Entrevista entrevista = buscarPorId(entrevistaId);
        if (entrevista == null) {
            throw new RuntimeException("Entrevista não encontrada.");
        }
        Candidatura candidatura = entrevista.getCandidatura();
        if (candidatura == null) {
            throw new RuntimeException("Candidatura não encontrada para a entrevista.");
        }
        // Atualiza status da candidatura para APROVADA
        candidatura.setStatus(Candidatura.StatusCandidatura.SELECIONADO);
        candidaturaService.salvar(candidatura);

        // Fecha a vaga
        Vaga vaga = candidatura.getVaga();
        vaga.setStatusProcesso(StatusProcesso.FINALIZADA);
        vagaService.salvar(vaga);

        // Envia e-mail para a empresa cliente
        String emailEmpresa = vaga.getCliente().getContato().getEmail();
        String assunto = "Encaminhamento de Candidato Aprovado - Vakantes";
        String corpo = "Prezado(a),\n\n" +
                "O candidato " + candidatura.getCandidato().getNomeCandidato() +
                " foi aprovado na entrevista para a vaga: " + vaga.getNome() + ".\n\n" +
                "Informações do candidato:\n" +
                "Nome: " + candidatura.getCandidato().getNomeCandidato() + "\n" +
                "E-mail: " + candidatura.getCandidato().getContato().getEmail() + "\n" +
                "Telefone: " + candidatura.getCandidato().getContato().getTelefone() + "\n\n" +
                "Informações da vaga:\n" +
                "Vaga: " + vaga.getNome() + "\n" +
                "Descrição: " + vaga.getDescricao() + "\n\n" +
                "Por favor, prossiga com o processo de contratação.\n\n" +
                "Atenciosamente,\nEquipe Vakantes";
        emailService.enviarEmailSimples(emailEmpresa, assunto, corpo);
    }

}
