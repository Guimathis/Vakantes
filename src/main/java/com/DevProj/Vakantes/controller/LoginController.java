package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.model.usuario.UsuarioDTO;
import com.DevProj.Vakantes.repository.UsuarioRepository;
import com.DevProj.Vakantes.service.CookieService;
import com.DevProj.Vakantes.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @PostMapping("/logar")
    public String logar(UsuarioDTO usuarioDTO, HttpServletResponse response, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
        Usuario usuarioLogado = usuarioService.autenticarUsuario(usuarioDTO.getUsername(), usuarioDTO.getPassword());

        if (usuarioLogado != null) {
            CookieService.setCookie(response, "usuarioId", String.valueOf(usuarioLogado.getId()), 10000);
            return "redirect:/home/index";
        }
        redirectAttributes.addFlashAttribute("mensagem_erro", "Email ou senha incorretos.");
        return "redirect:/auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) throws UnsupportedEncodingException {
        CookieService.removeCookie(response, "usuarioId");
        return "redirect:/auth/login";
    }

}
