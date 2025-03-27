package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.Usuario;
import com.DevProj.Vakantes.service.CookieService;
import com.DevProj.Vakantes.service.UsuarioService;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.buscarTodos());
        return "/list";
    }

    @GetMapping("/{id}")
    public String perfil(@PathVariable Long id, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
        String usuarioId = CookieService.getCookie(request, "usuarioId");
        Optional<Usuario> usuarioRequisitou = usuarioService.buscarPorId(Long.parseLong(usuarioId));
        if(usuarioRequisitou.isEmpty()) {
            throw new ObjectNotFoundException("Usuário não encontrado.");
        }
        Optional<Usuario> usuarioRequisitado = usuarioService.buscarPorId(id);
        if(usuarioRequisitado.isEmpty()) {
            throw new ObjectNotFoundException("Usuário não encontrado.");
        }
        if ((!String.valueOf(id).equals(usuarioId)) && !usuarioRequisitou.get().getUserRole().name().equals("ADMIN")) {
            throw new ObjectNotFoundException("Voce não tem permissao");
        }
        if(Objects.equals(usuarioRequisitado.get().getId(), usuarioRequisitou.get().getId())) {
            model.addAttribute("nomeUsuario", usuarioRequisitado.get().getNomeCompleto());
        } else {
            model.addAttribute("nomeUsuario", usuarioRequisitou.get().getNomeCompleto());
        }
        model.addAttribute("usuario", usuarioRequisitado.get());
        return "entities/usuario/perfil";
    }


    @PostMapping("/salvar")
    public String editarUsuario(@ModelAttribute Usuario usuario, HttpServletResponse response) throws UnsupportedEncodingException {
        usuarioService.atualizar(usuario);
        CookieService.setCookie(response, "nomeUsuario", usuario.getNomeCompleto(), 10000);
        return "redirect:/usuario/" + usuario.getId();
    }

    @GetMapping("/edit/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.buscarPorId(id));
        return "/form";
    }

    @GetMapping("/delete/{id}")
    public String excluirUsuario(@PathVariable Long id) {
        usuarioService.deletar(id);
        return "redirect:/usuarios";
    }
}