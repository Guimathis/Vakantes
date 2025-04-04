package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.enums.TipoPessoa;
import com.DevProj.Vakantes.model.vaga.VagaDTO;
import com.DevProj.Vakantes.repository.CandidatoRepository;
import com.DevProj.Vakantes.service.CandidatoService;
import com.DevProj.Vakantes.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/candidato")
public class CandidatoController {
    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    CandidatoService candidatoService;

    @GetMapping(value = "/cadastro")
    public String form(Model model) {
        model.addAttribute("candidato" , new Candidato());
        return "entities/candidato/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(Candidato candidato, Model model, RedirectAttributes attributes){
        if(candidato.getId() == 0){
            candidatoService.cadastrar(candidato);
            model.addAttribute("candidatos", candidatoService.buscarTodos());
            return "redirect:/candidato/buscar";
        } else {
            candidatoService.editar(candidato);
            model.addAttribute("candidato" , candidato);
            attributes.addFlashAttribute("mensagem", "Candidato editado com sucesso!");
            model.addAttribute("editar", false);
            return "redirect:/candidato/buscar/" + candidato.getId();
        }

    }

    @GetMapping("/buscar")
    public String listaCandidatos(Model model)  {
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
