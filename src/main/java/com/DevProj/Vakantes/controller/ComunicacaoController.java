package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.entrevista.Entrevista;
import com.DevProj.Vakantes.service.ComunicacaoService;
import com.DevProj.Vakantes.service.EntrevistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/comunicacao")
public class ComunicacaoController {

    @Autowired
    private EntrevistaService entrevistaService;

    @Autowired
    private ComunicacaoService comunicacaoService;

    @PostMapping(value = "/comunicar-candidato", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> comunicarCandidato(@RequestBody Map<String, String> payload) {
        Map<String, Object> resp = new HashMap<>();
        try {
            String email = payload.get("email");
            String mensagem = payload.get("mensagem");
            Long entrevistaId = Long.parseLong(payload.get("entrevistaId"));
            Long comunicacaoId = Long.parseLong(payload.get("comunicacaoId") != null ? payload.get("comunicacaoId") : "0");

            if (comunicacaoId > 0) {
                // Reenvio de comunicação existente
                comunicacaoService.reenviarComunicacao(comunicacaoId, mensagem);
            } else {
                // Nova comunicação
                Entrevista entrevista = entrevistaService.buscarPorId(entrevistaId);
                comunicacaoService.salvarEEnviarComunicacao(entrevista, email, mensagem);
            }

            resp.put("sucesso", true);
        } catch (Exception e) {
            resp.put("sucesso", false);
            resp.put("mensagem", e.getMessage());
        }
        return resp;
    }
}
