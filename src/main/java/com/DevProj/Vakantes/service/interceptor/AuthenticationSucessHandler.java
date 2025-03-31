package com.DevProj.Vakantes.service.interceptor;

import com.DevProj.Vakantes.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationSucessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UsuarioService usuarioService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        HttpSession session = request.getSession();
        session.setAttribute("nomeUsuario", usuarioService.buscarPorEmail(userDetails.getUsername()).getPrimeiroNome());
        response.sendRedirect("/"); // Redireciona para a página inicial
    }
}
