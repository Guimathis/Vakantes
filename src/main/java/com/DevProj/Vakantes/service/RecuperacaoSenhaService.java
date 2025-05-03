package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Service
public class RecuperacaoSenhaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // token numérico de 6 dígitos
    public String gerarToken() {
        Random random = new Random();
        int numero = 100000 + random.nextInt(900000);
        return String.valueOf(numero);
    }

    public boolean solicitarRecuperacao(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return false;
        }

        // Gerar token e definir expiração (30 minutos)
        String token = gerarToken();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        Date tokenExpiry = calendar.getTime();

        // Salvar token no usuário
        usuario.setResetToken(token);
        usuario.setResetTokenExpiry(tokenExpiry);
        usuarioRepository.save(usuario);

        // Enviar email com o token
        emailService.enviarEmailRecuperacaoSenha(email, token);

        return true;
    }

    // Validar token
    public boolean validarToken(String email, String token) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return false;
        }

        // Verificar se o token é válido e não expirou
        return token.equals(usuario.getResetToken()) &&
                new Date().before(usuario.getResetTokenExpiry());
    }

    // Redefinir senha
    public boolean redefinirSenha(String email, String token, String novaSenha) {
        if (!validarToken(email, token)) {
            return false;
        }

        Usuario usuario = usuarioRepository.findByEmail(email);

        // Atualizar senha e limpar token
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setResetToken(null);
        usuario.setResetTokenExpiry(null);
        usuarioRepository.save(usuario);

        return true;
    }
}