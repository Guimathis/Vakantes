package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode acessar
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

//    @GetMapping
//    public String listarUsuarios(Model model) {
//        model.addAttribute("usuarios", usuarioService.buscarTodos());
//        return "admin/usuarios/list"; // Mapeia para templates/admin/usuarios/list.html
//    }
//
//    @GetMapping("/novo")
//    public String novoUsuario(Model model) {
//        model.addAttribute("usuario", new Usuario());
//        return "admin/usuarios/form"; // Mapeia para templates/admin/usuarios/form.html
//    }
//
//    @PostMapping("/salvar")
//    public String salvarUsuario(@ModelAttribute Usuario usuario) {
//        usuarioService.salvar(usuario);
//        return "redirect:/admin/usuarios";
//    }
//
//    @GetMapping("/edit/{id}")
//    public String editarUsuario(@PathVariable Long id, Model model) {
//        model.addAttribute("usuario", usuarioService.buscarPorId(id));
//        return "admin/usuarios/form";
//    }
//
//    @GetMapping("/delete/{id}")
//    public String excluirUsuario(@PathVariable Long id) {
//        usuarioService.excluir(id);
//        return "redirect:/admin/usuarios";
//    }
}