package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.service.RecuperacaoSenhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth/recuperar-senha")
public class RecuperacaoSenhaController {

    @Autowired
    private RecuperacaoSenhaService recuperacaoSenhaService;

    // Página de solicitação de recuperação
    @GetMapping
    public String exibirFormularioSolicitacao() {
        return "auth/recuperar-senha";
    }

    // Processar solicitação
    @PostMapping("/solicitar")
    public String processarSolicitacao(@RequestParam("email") String email,
                                       RedirectAttributes attributes) {
        boolean enviado = recuperacaoSenhaService.solicitarRecuperacao(email);

        if (enviado) {
            attributes.addFlashAttribute("mensagem",
                    "Um código de verificação foi enviado para seu email.");
            attributes.addFlashAttribute("email", email);
            return "redirect:/auth/recuperar-senha/verificar";
        } else {
            attributes.addFlashAttribute("mensagem_erro",
                    "Email não encontrado no sistema.");
            return "redirect:/auth/recuperar-senha";
        }
    }

    @GetMapping("/verificar")
    public String exibirFormularioVerificacao(Model model) {
        if (!model.containsAttribute("email")) {
            return "redirect:/auth/recuperar-senha";
        }
        return "auth/verificar-codigo";
    }

    @PostMapping("/verificar")
    public String processarVerificacao(@RequestParam("email") String email,
                                       @RequestParam("token") String token,
                                       RedirectAttributes attributes) {
        boolean valido = recuperacaoSenhaService.validarToken(email, token);

        if (valido) {
            attributes.addFlashAttribute("email", email);
            attributes.addFlashAttribute("token", token);
            return "redirect:/auth/recuperar-senha/redefinir";
        } else {
            attributes.addFlashAttribute("mensagem_erro",
                    "Código inválido ou expirado.");
            attributes.addFlashAttribute("email", email);
            return "redirect:/auth/recuperar-senha/verificar";
        }
    }

    @GetMapping("/redefinir")
    public String exibirFormularioRedefinicao(Model model) {
        if (!model.containsAttribute("email") || !model.containsAttribute("token")) {
            return "redirect:/auth/recuperar-senha";
        }
        return "auth/redefinir-senha";
    }

    // Processar redefinição
    @PostMapping("/redefinir")
    public String processarRedefinicao(@RequestParam("email") String email,
                                       @RequestParam("token") String token,
                                       @RequestParam("novaSenha") String novaSenha,
                                       @RequestParam("confirmarSenha") String confirmarSenha,
                                       RedirectAttributes attributes) {
        if (!novaSenha.equals(confirmarSenha)) {
            attributes.addFlashAttribute("mensagem_erro", "As senhas não coincidem.");
            attributes.addFlashAttribute("email", email);
            attributes.addFlashAttribute("token", token);
            return "redirect:/auth/recuperar-senha/redefinir";
        }

        boolean redefinido = recuperacaoSenhaService.redefinirSenha(email, token, novaSenha);

        if (redefinido) {
            attributes.addFlashAttribute("mensagem",
                    "Senha redefinida com sucesso! Você já pode fazer login.");
            return "redirect:/auth/login";
        } else {
            attributes.addFlashAttribute("mensagem_erro",
                    "Não foi possível redefinir a senha. Tente novamente.");
            return "redirect:/auth/recuperar-senha";
        }
    }
}