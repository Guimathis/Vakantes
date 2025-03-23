package com.DevProj.Vakantes.config.secutity;

import com.DevProj.Vakantes.model.Usuario;
import com.DevProj.Vakantes.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieAuthFilter extends OncePerRequestFilter {

    private final UsuarioRepository usuarioRepository;

    public CookieAuthFilter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Cookie usuarioIdCookie = Arrays.stream(cookies)
                    .filter(cookie -> "usuarioId".equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);

            if (usuarioIdCookie != null) {
                try {
                    Long usuarioId = Long.parseLong(usuarioIdCookie.getValue());

                    // Busca o usuário real no banco pelo ID
                    Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);

                    if (usuarioOptional.isPresent()) {
                        Usuario usuario = usuarioOptional.get();

                        // Criando UserDetails a partir do usuário encontrado
                        UserDetails userDetails = User.withUsername(usuario.getEmail())
                                .password(usuario.getSenha()) // Senha não será usada, pois estamos usando cookies
                                .roles(usuario.getUserRole().name()) // Assumindo que userRole é um ENUM
                                .build();

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (NumberFormatException e) {
                    // Caso o ID no cookie não seja válido, ignoramos a autenticação
                    System.err.println("Erro ao converter usuarioId para Long: " + e.getMessage());
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

