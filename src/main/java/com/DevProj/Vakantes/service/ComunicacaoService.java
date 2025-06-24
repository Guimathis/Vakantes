package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.comunicacao.Comunicacao;
import com.DevProj.Vakantes.model.entrevista.Entrevista;
import com.DevProj.Vakantes.model.entrevista.enums.StatusNotificacaoEmail;
import com.DevProj.Vakantes.repository.ComunicacaoRepository;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComunicacaoService {

    @Autowired
    private ComunicacaoRepository comunicacaoRepository;

    @Autowired
    private EmailService emailService;

    public void salvarComunicacao(Comunicacao comunicacao) {
        comunicacaoRepository.save(comunicacao);
    }

    public void comunicacaoNovaEntrevista(Entrevista entrevista, String email, String mensagem) {
        Comunicacao comunicacao = new Comunicacao();
        comunicacao.setEntrevista(entrevista);
        comunicacao.setEmail(email);
        comunicacao.setMensagem(mensagem);
        comunicacao.setStatusEnvio(StatusNotificacaoEmail.PENDENTE); // Inicialmente não enviado
        comunicacaoRepository.save(comunicacao);
    }

    public void salvarEEnviarComunicacao(Entrevista entrevista, String email, String mensagem) {
        Comunicacao comunicacao = new Comunicacao();
        comunicacao.setEntrevista(entrevista);
        comunicacao.setEmail(email);
        comunicacao.setMensagem(mensagem);
        comunicacaoRepository.save(comunicacao);

        boolean enviado = emailService.enviarEmailSimples(email, "Comunicação sobre entrevista", mensagem);
        if (enviado) {
            comunicacao.setStatusEnvio(StatusNotificacaoEmail.ENVIADO);
        }

        comunicacaoRepository.save(comunicacao);
    }

    public List<Comunicacao> buscarPorEntrevista(Entrevista entrevista) {
        return comunicacaoRepository.findByEntrevistaOrderByDataComunicacaoDesc(entrevista);
    }

    public void reenviarComunicacao(Long comunicacaoId, String mensagem) {
        Comunicacao comunicacao = comunicacaoRepository.findComunicacaoById(comunicacaoId);
        if (comunicacao == null) {
            throw new ObjectNotFoundException("Ocorreu um erro ao reenviar a mensagem");
        }
        comunicacao.setMensagem(mensagem);
        comunicacao.setDataComunicacao(LocalDateTime.now());
        boolean enviado = emailService.enviarEmailSimples(comunicacao.getEmail(), "Comunicação sobre entrevista", comunicacao.getMensagem());
        if (enviado) {
            comunicacao.setStatusEnvio(StatusNotificacaoEmail.ENVIADO);
        }
        comunicacaoRepository.save(comunicacao);
    }

    public Comunicacao novaComunicacao(String email, String observacoes) {
        Comunicacao comunicacao = new Comunicacao(observacoes, email);
        comunicacao.setDataComunicacao(LocalDateTime.now());
        return comunicacaoRepository.save(comunicacao);
    }
}