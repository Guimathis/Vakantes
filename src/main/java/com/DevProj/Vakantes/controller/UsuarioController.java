package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.Usuario;
import com.DevProj.Vakantes.service.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMIN')") // Somente ADMIN pode cadastrar usuários
    @PostMapping("/registrar")
    public Usuario registrarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.salvarUsuario(usuario);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listar")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }
}