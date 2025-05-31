package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.model.empresa.enums.TipoPessoa;
import com.DevProj.Vakantes.model.usuario.enums.UserRole;
import com.DevProj.Vakantes.service.ClienteService;
import com.DevProj.Vakantes.service.exceptions.DataBindingViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("tiposPessoa", TipoPessoa.values());
        model.addAttribute("cliente", new Cliente());
        return "entities/cliente/cadastro";
    }

    @GetMapping("/cadastro/{id}")
    public String buscarCliente(@PathVariable Long id, Model model) {
        model.addAttribute("tiposPessoa", TipoPessoa.values());
        model.addAttribute("cliente", clienteService.buscarClientePorId(id));
        return "entities/cliente/cadastro";
    }

    @PostMapping("/salvar")
    public String salvarCliente(@ModelAttribute Cliente cliente, @ModelAttribute("currentUser") Usuario currentUser, RedirectAttributes attributes, Model model) {
        try {
            Long usuarioId = (Long) model.getAttribute("usuarioId");
            currentUser.setId(usuarioId);
            cliente.setUsuarioResponsavel(currentUser);
            clienteService.salvarCliente(cliente);
            attributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
            return "redirect:/cliente/buscar";
        } catch (DataBindingViolationException e) {
            model.addAttribute("mensagem_erro", e.getMessage());
            model.addAttribute("tiposPessoa", TipoPessoa.values());
            model.addAttribute("cliente", cliente);
            return "entities/cliente/cadastro";
        } catch (Exception e) {
            model.addAttribute("mensagem_erro", "Ocorreu um erro ao salvar o cliente: " + e.getMessage());
            model.addAttribute("tiposPessoa", TipoPessoa.values());
            model.addAttribute("cliente", cliente);
            return "entities/cliente/cadastro";
        }
    }

@GetMapping("/buscar")
public String listarClientes(Model model, @ModelAttribute("currentUser") Usuario currentUser) {
    if (currentUser.getUserRole().equals(UserRole.ADMIN)) {
        model.addAttribute("clientes", clienteService.buscarTodos());
    } else {
        model.addAttribute("clientes", clienteService.buscarClientePorResponsavel(currentUser));
    }
    return "entities/cliente/buscar";
}

    @GetMapping("/buscar/{id}")
    public String listarCliente(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.buscarClientePorId(id));
        model.addAttribute("tiposPessoa", TipoPessoa.values());
        model.addAttribute("editar", false);
        return "entities/cliente/cadastro";
    }

    @GetMapping(value = "/editar/{id}")
    public String editarCliente(Model model, @PathVariable long id) {
        model.addAttribute("cliente", clienteService.buscarClientePorId(id));
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
