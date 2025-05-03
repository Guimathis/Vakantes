package com.DevProj.Vakantes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
}