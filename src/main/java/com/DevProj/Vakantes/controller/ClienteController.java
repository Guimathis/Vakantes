package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.enums.TipoPessoa;
import com.DevProj.Vakantes.model.util.Status;
import com.DevProj.Vakantes.service.ClienteService;
import com.DevProj.Vakantes.service.CookieService;
import com.DevProj.Vakantes.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private UsuarioService usuarioService;


    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        model.addAttribute("idUsuario", CookieService.getCookie(request, "usuarioId"));
        model.addAttribute("tiposPessoa", TipoPessoa.values());
        model.addAttribute("cliente", new Cliente());
        return "entities/cliente/cadastro";
    }

    @GetMapping("/cadastro/{id}")
    public String buscarCliente(@PathVariable Long id, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        model.addAttribute("idUsuario", CookieService.getCookie(request, "usuarioId"));
        model.addAttribute("tiposPessoa", TipoPessoa.values());
        model.addAttribute("cliente", clienteService.buscarClientePorIdAndStatus(id, Status.ATIVO));
        return "entities/cliente/cadastro";
    }

    @PostMapping("/salvar")
    public String salvarCliente(@ModelAttribute Cliente cliente, HttpServletRequest request) throws UnsupportedEncodingException {
        Long idUser = Long.valueOf(CookieService.getCookie(request, "usuarioId"));
        cliente.setUsuarioResponsavel(usuarioService.buscarPorId(idUser).get());
        clienteService.salvarCliente(cliente);
        return "redirect:/cliente/buscar";
    }

    @GetMapping("/buscar")
    public String listarClientes(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        String idUsuario = CookieService.getCookie(request, "usuarioId");
        model.addAttribute("idUsuario",idUsuario);
        model.addAttribute("clientes", clienteService.buscarClientePorResponsavel(usuarioService.buscarPorId(Long.valueOf(idUsuario)).get()));
        return "entities/cliente/buscar";
    }

    @GetMapping("/buscar/{id}")
    public String listarCliente(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.buscarClientePorIdAndStatus(id, Status.ATIVO));
        model.addAttribute("tiposPessoa", TipoPessoa.values());
        model.addAttribute("editar", false);
        return "entities/cliente/cadastro";
    }

    @GetMapping(value = "/editar/{id}")
    public String editarCliente(Model model, @PathVariable long id) {
        model.addAttribute("cliente", clienteService.buscarClientePorIdAndStatus(id, Status.ATIVO));
        model.addAttribute("tiposPessoa", TipoPessoa.values());
        model.addAttribute("editar", true);
        return "entities/cliente/cadastro";
    }

    @GetMapping("/deletar/{id}")
    public String deletarCliente(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            clienteService.deletarCliente(id);
            attributes.addFlashAttribute("mensagem", "Candidato deletado com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagem_erro", e.getMessage());
        }
        return "redirect:/cliente/buscar";
    }

}
