package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.vaga.Candidatura;
import com.DevProj.Vakantes.model.vaga.Vaga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmailRecuperacaoSenha(String destinatario, String token) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destinatario);
        mensagem.setSubject("Vakantes - Recuperação de Senha");
        mensagem.setText("Olá,\n\n" +
                "Você solicitou a recuperação de senha para sua conta no Vakantes.\n" +
                "Use o código a seguir para redefinir sua senha: " + token + "\n\n" +
                "Este código expira em 30 minutos.\n\n" +
                "Se você não solicitou esta alteração, ignore este email.\n\n" +
                "Atenciosamente,\n" +
                "Equipe Vakantes");

        mailSender.send(mensagem);
    }

    public void enviarEmailAgendamentoEntrevista(Candidato candidato, Vaga vaga, String dataHora, String local, String observacoes) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(candidato.getContato().getEmail());
        mensagem.setSubject("Vakantes - Agendamento de Entrevista");
        mensagem.setText("Olá " + candidato.getNomeCandidato() + ",\n\n" +
                "Sua entrevista para a vaga: " + vaga.getNome() + ", foi agendada!\n\n" +
                "Data e horário: " + LocalDateTime.parse(dataHora).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n" +
                "Local: " + local + "\n" +
                (observacoes != null && !observacoes.isEmpty() ? "Observações: " + observacoes + "\n" : "") +
                "Atenciosamente,\n" +
                "Equipe Vakantes");

        try {
            mailSender.send(mensagem);
        } catch (MailException e) {
            throw new RuntimeException("Ocorreu um erro ao enviar o email de agendamento.");
        }
    }

    public void notificarCandidatoSelecionado(Candidatura candidatura) {
        Candidato candidato = candidatura.getCandidato();
        Vaga vaga = candidatura.getVaga();
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(candidato.getContato().getEmail());
        mensagem.setSubject("Vakantes - Parabéns! Você foi selecionado");
        mensagem.setText("Olá " + candidato.getNomeCandidato() + ",\n\n" +
                "Parabéns! Você foi selecionado para a vaga: " + vaga.getNome() + ".\n" +
                "Em breve " + vaga.getCliente().getNome() + " em contato para os próximos passos.\n\n" +
                "Atenciosamente,\nEquipe Vakantes");
        try {
            mailSender.send(mensagem);
        } catch (MailException e) {
            throw new RuntimeException("Ocorreu um erro ao enviar o email de seleção.");
        }
    }

    public boolean enviarEmailSimples(String destinatario, String assunto, String mensagemTexto) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destinatario);
        mensagem.setSubject(assunto);
        mensagem.setText(mensagemTexto);
        try {
            mailSender.send(mensagem);
            return true;
        } catch (MailException e) {
            throw new RuntimeException("Ocorreu um erro ao enviar o e-mail: " + e.getMessage());
        }
    }
}
