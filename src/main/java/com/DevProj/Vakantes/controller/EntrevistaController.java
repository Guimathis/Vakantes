package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.comunicacao.Comunicacao;
import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.entrevista.Entrevista;
import com.DevProj.Vakantes.model.vaga.Candidatura;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.service.*;
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
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
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

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/cadastrar")
    public String salvarEntrevista(
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

    @GetMapping("/buscar")
    public String listarEntrevistas(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) Long vagaId,
            @RequestParam(required = false) Long candidatoId,
            @RequestParam(required = false) String status,
            Model model) {

        // Adicionar listas para os dropdowns
        model.addAttribute("clientes", clienteService.buscarTodos());
        model.addAttribute("vagas", vagaService.buscarTodas());
        model.addAttribute("candidatos", candidatoService.buscarTodos());

        try {

            // Definir o status padrão como INSCRITO se não for especificado
            Candidatura.StatusCandidatura statusFilter = null;
            if (status != null) {
                if (status.equals("INSCRITO") || status.equals("SELECIONADO") || status.equals("REJEITADO")) {
                    statusFilter = Candidatura.StatusCandidatura.valueOf(status);
                }
            } else {
                // Aplicar filtro padrão INSCRITO apenas se nenhum status foi especificado
                statusFilter = Candidatura.StatusCandidatura.INSCRITO;
                model.addAttribute("statusDefault", true);
            }

            // Aplicar filtros
            model.addAttribute("entrevistas", entrevistaService.buscarComFiltros(clienteId, vagaId, candidatoId, statusFilter));
        } catch (Exception e) {
            model.addAttribute("mensagem_erro", "Erro ao aplicar filtros: " + e.getMessage());
            model.addAttribute("entrevistas", entrevistaService.buscarTodas());
        }

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
            Entrevista entrevista = entrevistaService.buscarPorId(id);
            if (entrevista.getCandidatura() != null && entrevista.getCandidatura().getStatus() != null &&
                !entrevista.getCandidatura().getStatus().name().equals("INSCRITO")) {
                resp.put("sucesso", false);
                resp.put("mensagem", "Não é possível editar uma entrevista já realizada ou finalizada.");
                return resp;
            }
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

    @PostMapping("/{id}/aprovar")
    @ResponseBody
    public Map<String, Object> aprovarCandidaturaNaEntrevista(@PathVariable Long id) {
        Map<String, Object> resp = new HashMap<>();
        try {
            entrevistaService.aprovarCandidaturaNaEntrevista(id);
            resp.put("sucesso", true);
            resp.put("mensagem", "Candidatura aprovada e empresa notificada por e-mail.");
        } catch (Exception e) {
            resp.put("sucesso", false);
            resp.put("mensagem", e.getMessage());
        }
        return resp;
    }

    @PostMapping(value = "/realizar", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> realizarEntrevista(@RequestBody Map<String, String> payload) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Long entrevistaId = Long.parseLong(payload.get("entrevistaId"));
            Long candidaturaId = Long.parseLong(payload.get("candidaturaId"));
            String status = payload.get("status");
            String observacoes = payload.get("observacoes");
            entrevistaService.realizarEntrevista(entrevistaId, candidaturaId, status, observacoes);
            resp.put("sucesso", true);
        } catch (Exception e) {
            resp.put("sucesso", false);
            resp.put("mensagem", e.getMessage());
        }
        return resp;
    }

    @GetMapping("/enderecos")
    @ResponseBody
    public List<Map<String, Object>> getEnderecosPorVaga(@RequestParam("vagaId") Long vagaId) {
        Vaga vaga = vagaService.buscarVagaPorID(vagaId);
        Cliente cliente = vaga.getCliente();
        List<Map<String, Object>> enderecos = new java.util.ArrayList<>();
        if (cliente != null && cliente.getEnderecos() != null) {
            for (com.DevProj.Vakantes.model.util.Endereco endereco : cliente.getEnderecos()) {
                Map<String, Object> enderecoMap = new HashMap<>();
                enderecoMap.put("id", endereco.getId());
                enderecoMap.put("descricao", endereco.getRua() + ", " + endereco.getNumero() +
                        " - " + endereco.getBairro() + ", " + endereco.getCidade() + "/" + endereco.getEstado());
                enderecos.add(enderecoMap);
            }
        }
        return enderecos;
    }
}
