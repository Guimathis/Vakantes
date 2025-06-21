package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.util.enums.Status;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.model.vaga.VagaDTO;
import com.DevProj.Vakantes.repository.CandidatoRepository;
import com.DevProj.Vakantes.repository.ClienteRepository;
import com.DevProj.Vakantes.repository.VagaRepository;
import com.DevProj.Vakantes.service.*;
import com.DevProj.Vakantes.service.exceptions.DataBindingViolationException;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller("VC")
@RequestMapping("/vaga")
public class VagaController {

    @Autowired
    private VagaRepository vr;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    ClienteService clienteService;

    @Autowired
    VagaService vagaService;

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private MatchingService matchingService;

    // CADASTRA VAGA
    @GetMapping(value = "/cadastro")
    public String form(Model model) {
        model.addAttribute("clientes", clienteService.buscarTodos());
        model.addAttribute("vaga", model.getAttribute("vaga") == null ? new VagaDTO() : model.getAttribute("vaga"));
        return "entities/vaga/cadastro";
    }

    @GetMapping(value = "/cadastro/{id}")
    public String form(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        VagaDTO vaga = new VagaDTO();

        try {
            Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado"));
            vaga.setIdCliente(cliente.getId());
            model.addAttribute("clientes", clienteService.buscarTodos());
            model.addAttribute("vaga", vaga);
        } catch (ObjectNotFoundException e) {
            attributes.addFlashAttribute("mensagem_erro", e.getMessage());
        }

        return "entities/vaga/cadastro";
    }

    @PostMapping("/salvar")
    public String form(@Valid VagaDTO vagaDTO, BindingResult result, RedirectAttributes attributes, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("vaga", vagaDTO);
            model.addAttribute("clientes", clienteService.buscarTodos());
            attributes.addFlashAttribute("mensagem_erro", result.getAllErrors().get(0).getDefaultMessage());
            attributes.addFlashAttribute("vaga", vagaDTO);
            return "redirect:/vaga/cadastro";
        }

        Long id = vagaService.cadastrarVaga(vagaDTO);

        if (vagaDTO.getCodigo() != 0) {
            attributes.addFlashAttribute("mensagem", "Vaga editada com sucesso!");
        } else {
            attributes.addFlashAttribute("mensagem", "Vaga cadastrada com sucesso!");
        }
        return "redirect:/vaga/buscar/" + id;
    }

    // LISTA VAGAS
    @GetMapping("/buscar")
    public String listaVaga(Model model) {
        model.addAttribute("vagas", vagaService.buscarTodas());
        return "entities/vaga/buscar";
    }

    @GetMapping("/buscar/{codigo}")
    public String detalhesVaga(@PathVariable("codigo") Long codigo, Model model) {
        Vaga vaga = vr.findByCodigoAndStatus(codigo, Status.ATIVO).orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));
        VagaDTO vagaDTO = new VagaDTO(vaga);
        model.addAttribute("vaga", vagaDTO);
        model.addAttribute("clientes", clienteService.buscarTodos());
        model.addAttribute("editar", false);
        return "entities/vaga/cadastro";
    }

    // Métodos que atualizam vaga
    // formulário edição de vaga
    @GetMapping(value = "/editar/{codigo}")
    public String editarVaga(Model model, @PathVariable Long codigo) {
        Vaga vaga = vr.findByCodigoAndStatus(codigo, Status.ATIVO).orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));
        VagaDTO vagaDTO = new VagaDTO(vaga);
        model.addAttribute("vaga", vagaDTO);
        model.addAttribute("clientes", clienteService.buscarTodos());
        model.addAttribute("editar", true);
        return "entities/vaga/cadastro";
    }


    @GetMapping(value = "/detalhes/{codigo}")
    public String detalhesVaga(Model model, @PathVariable Long codigo) {
        Vaga vaga = vr.findByCodigoAndStatus(codigo, Status.ATIVO).orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));
        VagaDTO vagaDTO = new VagaDTO(vaga);
        model.addAttribute("vaga", vagaDTO);
        model.addAttribute("candidatosSistema", candidatoRepository.findAll());
        return "entities/vaga/detalhes";
    }

    @PostMapping("/candidato/existente/{id}")
    public String associarCandidatoExistente(@PathVariable("id") Long vagaId, @RequestParam(required = false) List<String> cpfs,
                                             RedirectAttributes redirectAttributes) {
        try {
            vagaService.inscreverCandidatos(vagaId, cpfs);
            redirectAttributes.addFlashAttribute("mensagem", "Candidato(s) inscrito(s) com sucesso!");
        } catch (DataBindingViolationException | ObjectNotFoundException e) {
            redirectAttributes.addFlashAttribute("mensagem_erro", e.getMessage());
        }
        return "redirect:/vaga/detalhes/" + vagaId;
    }

    @GetMapping("/selecao/{codigo}")
    public String candidatosInscritosComPontuacao(@PathVariable("codigo") Long codigo, Model model) {
        try {
            Vaga vaga = vr.findByCodigoAndStatus(codigo, Status.ATIVO)
                    .orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));

            List<MatchingService.CandidatoMatch> candidatosInscritosComPontuacao = matchingService.avaliarCandidatosVaga(codigo);

            model.addAttribute("vaga", new VagaDTO(vaga));
            model.addAttribute("candidaturas", candidatosInscritosComPontuacao);

            return "entities/vaga/selecao";
        } catch (Exception e) {
            model.addAttribute("mensagem_erro", e.getMessage());
            return "redirect:/vaga/buscar";
        }
    }

    @PostMapping("/selecionar/{codigoVaga}/{candidaturaId}")
    public String selecionarCandidato(
            @PathVariable Long codigoVaga,
            @PathVariable Long candidaturaId,
            RedirectAttributes redirectAttributes) {
        try {
            vagaService.selecionarCandidato(codigoVaga, candidaturaId);
            redirectAttributes.addFlashAttribute("mensagem", "Candidato selecionado com sucesso! A vaga foi finalizada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem_erro", e.getMessage());
        }
        return "redirect:/vaga/detalhes/" + codigoVaga;
    }

    @RequestMapping("/deletar/{id}")
    public String deletarVaga(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            vagaService.deletarVaga(id);
            attributes.addFlashAttribute("mensagem", "Vaga deletada com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagem_erro", e.getMessage());
        }
        return "redirect:/vaga/buscar";
    }

    @PostMapping("/{codigo}/remover/{id}")
    public String deletarCandidato(@PathVariable Long codigo, @PathVariable Long id, HttpServletRequest request, RedirectAttributes attributes) {
        if (codigo == null) {
            attributes.addFlashAttribute("mensagem_erro", "Vaga não encontrada");
            return "redirect:" + request.getHeader("Referer");
        }
        if (id == null) {
            attributes.addFlashAttribute("mensagem_erro", "Candidato não encontrado");
            return "redirect:" + request.getHeader("Referer");
        }

        vagaService.removerCandidatura(codigo, id);
        attributes.addFlashAttribute("mensagem", "Candidato removido com sucesso!");
        return "redirect:" + request.getHeader("Referer");
    }

    // UPDATE vaga
    @RequestMapping(value = "/editar-vaga", method = RequestMethod.POST)
    public String updateVaga(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {
        vr.save(vaga);
        attributes.addFlashAttribute("success", "Vaga alterada com sucesso!");

        Long codigoLong = vaga.getCodigo();
        String codigo = "" + codigoLong;
        return "redirect:/" + codigo;
    }

}
