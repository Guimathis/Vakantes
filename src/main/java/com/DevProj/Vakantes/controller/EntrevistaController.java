package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.comunicacao.Comunicacao;
import com.DevProj.Vakantes.model.entrevista.Entrevista;
import com.DevProj.Vakantes.model.vaga.Candidatura;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.service.*;
import com.DevProj.Vakantes.repository.CandidatoRepository;
import com.DevProj.Vakantes.repository.VagaRepository;
import com.DevProj.Vakantes.dto.CandidaturaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/entrevista")
public class EntrevistaController {

    @Autowired
    private EntrevistaService entrevistaService;

    @Autowired
    private VagaService vagaService;

    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    private CandidaturaService candidaturaService;

    @Autowired
    private ComunicacaoService comunicacaoService;

    @PostMapping("/cadastrar")
    public String salvarEntrevista(
            @RequestParam("vagaId") Long vagaId,
            @RequestParam("local") String local,
            @RequestParam("dataHora") String dataHora,
            @RequestParam("candidaturaId") Long candidaturaId,
            @RequestParam(value = "observacoes", required = false) String observacoes,
            RedirectAttributes redirectAttributes
    ) {
        try {
            entrevistaService.salvarEntrevista(candidaturaId, local, dataHora, observacoes, redirectAttributes);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem_erro", "Erro ao cadastrar entrevista: " + e.getMessage());
            return "redirect:/entrevista/buscar";
        }

        redirectAttributes.addFlashAttribute("mensagem", "Entrevista cadastrada com sucesso! O candidato será notificado por e-mail.");
        return "redirect:/entrevista/buscar";
    }

    @GetMapping("/cadastro/{vagaId}")
    public String cadastroEntrevistaVagaExistente(@RequestParam("vagaId") Long vagaId, Model model) {
        Vaga vaga;
        try {
            vaga = vagaService.buscarVagaPorID(vagaId);
        } catch (Exception e) {
            model.addAttribute("mensagem_erro", "Vaga não encontrada");
            return "redirect:/vaga/buscar";
        }
        model.addAttribute("vaga", vaga);
        model.addAttribute("candidatos", candidatoService.buscarTodos());
        model.addAttribute("entrevista", new Entrevista());
        return "entities/entrevista/cadastro";
    }

    @GetMapping("/cadastro")
    public String cadastroEntrevista(Model model) {
        Iterable<Vaga> vagas;
        vagas = vagaService.buscarTodas();
        model.addAttribute("vagas", vagas);
        model.addAttribute("entrevista", new Entrevista());
        return "entities/entrevista/cadastro";
    }

    @GetMapping("/buscar")
    public String listarEntrevistas(Model model) {
        model.addAttribute("entrevistas", entrevistaService.buscarTodas());
        model.addAttribute("vagas", vagaService.buscarTodas());
        return "entities/entrevista/buscar";
    }

    @GetMapping("/candidatos")
    @ResponseBody
    public List<CandidaturaDTO> getCandidatosPorVaga(@RequestParam("vagaId") Long vagaId) {
        Vaga vaga = vagaService.buscarVagaPorID(vagaId);
        List<Candidatura> candidaturas = candidaturaService.buscarPorVagaId(vaga);
        return candidaturas.stream()
                .map(c -> new CandidaturaDTO(c.getId(), c.getCandidato().getNomeCandidato()))
                .collect(Collectors.toList());
    }

    @GetMapping("/detalhes-json/{id}")
    @ResponseBody
    public Map<String, Object> detalhesEntrevistaJson(@PathVariable Long id) {
        Entrevista entrevista = entrevistaService.buscarPorId(id);
        Map<String, Object> resp = new HashMap<>();
        resp.put("id", entrevista.getId());
        resp.put("local", entrevista.getLocal());
        resp.put("dataHora", entrevista.getDataHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        resp.put("observacoes", entrevista.getObservacoes());
        return resp;
    }

    @PostMapping(value = "/editar", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> editarEntrevista(@RequestBody Map<String, String> payload) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Long id = Long.parseLong(payload.get("id"));
            String local = payload.get("local");
            String dataHora = payload.get("dataHora");
            String observacoes = payload.get("observacoes");
            entrevistaService.editarEntrevista(id, local, dataHora, observacoes);
            resp.put("sucesso", true);
            resp.put("mensagem", true);
        } catch (Exception e) {
            resp.put("sucesso", false);
            resp.put("mensagem", e.getMessage());
        }
        return resp;
    }

    @GetMapping("/detalhes/{id}")
    public String detalhesEntrevista(@PathVariable Long id, Model model) {
        try {
            Entrevista entrevista = entrevistaService.buscarPorId(id);
            model.addAttribute("entrevista", entrevista);
            // Buscar histórico de comunicações
            List<Comunicacao> comunicacoes = comunicacaoService.buscarPorEntrevista(entrevista);
            model.addAttribute("comunicacoes", comunicacoes);
            return "entities/entrevista/detalhes";
        } catch (Exception e) {
            model.addAttribute("mensagem_erro", "Entrevista não encontrada");
            return "redirect:/entrevista/buscar";
        }
    }

    @GetMapping("/{id}/comunicacoes-html")
    public String comunicacoesHtml(@PathVariable Long id, Model model) {
        Entrevista entrevista = entrevistaService.buscarPorId(id);
        List<Comunicacao> comunicacoes = comunicacaoService.buscarPorEntrevista(entrevista);
        model.addAttribute("comunicacoes", comunicacoes);
        model.addAttribute("entrevista", entrevista);
        return "fragments/comunicacoes :: fragment";
    }
}
