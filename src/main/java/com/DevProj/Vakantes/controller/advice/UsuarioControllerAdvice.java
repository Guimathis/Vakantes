package com.DevProj.Vakantes.controller.advice;

import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.service.CookieService;
import com.DevProj.Vakantes.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * Controller advice para tornar as informações do usuário acessíveis em toda a aplicação.
 * Esta classe adiciona o usuário atual ao modelo para todos os controladores.
 *
 */
@ControllerAdvice
public class UsuarioControllerAdvice {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Adiciona o usuário atual ao modelo para todos os controladores.
     * Se o usuário não estiver logado, o atributo currentUser será nulo.
     *
     * @param model   O modelo para adicionar atributos
     * @param request A requisição HTTP
     * @return O usuário atual ou nulo se não estiver logado
     */
    @ModelAttribute("currentUser")
    public Usuario getCurrentUser(Model model, HttpServletRequest request) {
        try {
            String usuarioId = CookieService.getCookie(request, "usuarioId");
            if (usuarioId != null && !usuarioId.isEmpty()) {
                Optional<Usuario> usuario = usuarioService.buscarPorId(Long.valueOf(usuarioId));
                if (usuario.isPresent()) {
                    model.addAttribute("usuarioId", usuario.get().getId());
                    model.addAttribute("usuarioNome", usuario.get().getPrimeiroNome());
                    model.addAttribute("usuarioRole", usuario.get().getUserRole());
                    model.addAttribute("usuarioEmail", usuario.get().getEmail());
                    model.addAttribute("usuarioFoto", usuario.get().getFotoPerfil());

                    return usuario.get();
                }
            }
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error getting user from cookie: " + e.getMessage());
        }

        return null;
    }
}