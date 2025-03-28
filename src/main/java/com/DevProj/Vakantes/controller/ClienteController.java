package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.enums.TipoPessoa;
import com.DevProj.Vakantes.service.ClienteService;
import com.DevProj.Vakantes.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;


    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        model.addAttribute("nomeUsuario", CookieService.getCookie(request, "nomeUsuario"));
        model.addAttribute("idUsuario", CookieService.getCookie(request, "usuarioId"));
        model.addAttribute("tiposPessoa", TipoPessoa.values());
        model.addAttribute("cliente", new Cliente());
        return "entities/cliente/cadastro";
    }

    @GetMapping("/cadastro/{id}")
    public String buscarCliente(@PathVariable Long id, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        model.addAttribute("nomeUsuario", CookieService.getCookie(request, "nomeUsuario"));
        model.addAttribute("idUsuario", CookieService.getCookie(request, "usuarioId"));
        model.addAttribute("tiposPessoa", TipoPessoa.values());
        model.addAttribute("cliente", clienteService.buscarClientePorId(id));
        return "entities/cliente/cadastro";
    }

    @PostMapping("/salvar")
    public String salvarCliente(@ModelAttribute Cliente cliente) {
        cliente.setTipoPessoa(TipoPessoa.FISICA);
        clienteService.salvarCliente(cliente);
        return "redirect:/cliente/buscar";
    }

    @GetMapping("/buscar")
    public String listarClientes(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        model.addAttribute("nomeUsuario", CookieService.getCookie(request, "nomeUsuario"));
        model.addAttribute("idUsuario", CookieService.getCookie(request, "usuarioId"));
        model.addAttribute("clientes", clienteService.listarClientes());
        return "entities/cliente/buscar";
    }

    @GetMapping("/buscar/{id}")
    public String listarCliente(@PathVariable Long id, Model model) {
        model.addAttribute("busca", clienteService.buscarClientePorId(id));
        return "redirect:/cliente/cadastro/" + id;
    }
}
