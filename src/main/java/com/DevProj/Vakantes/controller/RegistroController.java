package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.service.UsuarioService;
import com.DevProj.Vakantes.service.exceptions.DataBindingViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model)  {
        model.addAttribute("usuario", model.getAttribute("usuario") == null ? new Usuario() : model.getAttribute("usuario"));
        return "admin/registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(Model model, @ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.salvar(usuario);
            redirectAttributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
            redirectAttributes.addFlashAttribute("usuario", usuario);
            return "redirect:/admin/registro";
        } catch (DataBindingViolationException e) {
            redirectAttributes.addFlashAttribute("usuario", usuario);
            redirectAttributes.addFlashAttribute("mensagem_erro", e.getMessage());
            return "redirect:/admin/registro";
        }
    }

    @GetMapping("/listar")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }
}