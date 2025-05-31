package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.service.CandidatoService;
import com.DevProj.Vakantes.service.exceptions.DataBindingViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/candidato")
public class CandidatoController {

    @Autowired
    CandidatoService candidatoService;

    @GetMapping(value = "/cadastro")
    public String form(Model model) {
        if (model.getAttribute("candidato") == null) {
            model.addAttribute("candidato", new Candidato());
        }
        return "entities/candidato/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(Candidato candidato, RedirectAttributes attributes) {
        try {
            if (candidato.getId() == null) {
                candidatoService.cadastrar(candidato);
                attributes.addFlashAttribute("mensagem", "Candidato cadastrado com sucesso!");
                return "redirect:/candidato/buscar";
            } else {
                candidatoService.editar(candidato);
                attributes.addFlashAttribute("mensagem", "Candidato editado com sucesso!");
                return "redirect:/candidato/buscar/" + candidato.getId();
            }
        } catch (DataBindingViolationException e) {
            attributes.addFlashAttribute("mensagem_erro", e.getMessage());
            attributes.addFlashAttribute("candidato", candidato);
            return "redirect:/candidato/cadastro";
        }
    }

    @GetMapping("/buscar")
    public String listaCandidatos(Model model) {
        model.addAttribute("candidatos", candidatoService.buscarTodos());
        return "entities/candidato/buscar";
    }

    @GetMapping("/buscar/{id}")
    public String buscarCandidato(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        try {
            model.addAttribute("candidato", candidatoService.buscarClientePorId(id));
            model.addAttribute("editar", false);
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagem_erro", e.getMessage());
        }
        return "entities/candidato/cadastro";
    }

    @GetMapping(value = "/editar/{id}")
    public String editarCandidato(Model model, @PathVariable long id, RedirectAttributes attributes) {
        try {
            model.addAttribute("candidato", candidatoService.buscarClientePorId(id));
            model.addAttribute("editar", true);
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagem_erro", e.getMessage());
        }
        return "entities/candidato/cadastro";
    }

    @GetMapping("/deletar/{id}")
    public String deletarCandidato(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            candidatoService.deletar(id);
            attributes.addFlashAttribute("mensagem", "Candidato deletado com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagem_erro", e.getMessage());
        }
        return "redirect:/candidato/buscar";
    }

}
