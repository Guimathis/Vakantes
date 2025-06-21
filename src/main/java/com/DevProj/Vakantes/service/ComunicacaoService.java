package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.comunicacao.Comunicacao;
import com.DevProj.Vakantes.model.entrevista.Entrevista;
import com.DevProj.Vakantes.repository.ComunicacaoRepository;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComunicacaoService {

    @Autowired
    private ComunicacaoRepository comunicacaoRepository;

    @Autowired
    private EmailService emailService;

    public void salvarEEnviarComunicacao(Entrevista entrevista, String email, String mensagem) {
        Comunicacao comunicacao = new Comunicacao();
        comunicacao.setEntrevista(entrevista);
        comunicacao.setEmail(email);
        comunicacao.setMensagem(mensagem);
        comunicacaoRepository.save(comunicacao);

        boolean enviado = emailService.enviarEmailSimples(email, "Comunicação sobre entrevista", mensagem);
        comunicacao.setEnviado(enviado);

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
        boolean enviado = emailService.enviarEmailSimples(comunicacao.getEmail(), "Comunicação sobre entrevista", comunicacao.getMensagem());
        comunicacao.setEnviado(enviado);
        comunicacaoRepository.save(comunicacao);
    }
}