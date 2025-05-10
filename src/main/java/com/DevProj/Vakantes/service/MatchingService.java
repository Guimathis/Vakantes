package com.DevProj.Vakantes.service;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.candidato.Habilidade;
import com.DevProj.Vakantes.model.util.Status;
import com.DevProj.Vakantes.model.vaga.Requisito;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.repository.CandidatoRepository;
import com.DevProj.Vakantes.repository.VagaRepository;
import com.DevProj.Vakantes.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private CandidatoRepository candidatoRepository;

    /**
     * Encontra candidatos adequados para uma vaga específica
     */
    public List<CandidatoMatch> encontrarCandidatosParaVaga(Long vagaId) {
        Vaga vaga = vagaRepository.findByCodigoAndStatus(vagaId, Status.ATIVO)
            .orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));

        Iterable<Candidato> candidatosIterable = candidatoRepository.findAllByStatus(Status.ATIVO);
        List<CandidatoMatch> matches = new ArrayList<>();

        // Convert Iterable to List
        List<Candidato> candidatos = new ArrayList<>();
        candidatosIterable.forEach(candidatos::add);

        for (Candidato candidato : candidatos) {
            int pontuacao = calcularPontuacao(vaga, candidato);
            Map<String, String> detalhes = calcularDetalhesCompatibilidade(vaga, candidato);

            if (pontuacao > 0) {
                matches.add(new CandidatoMatch(candidato, pontuacao, detalhes));
            }
        }

        // Ordenar por pontuação (do maior para o menor)
        matches.sort(Comparator.comparing(CandidatoMatch::getPontuacao).reversed());

        return matches;
    }

    /**
     * Encontra vagas adequadas para um candidato específico
     */
    public List<VagaMatch> encontrarVagasParaCandidato(Long candidatoId) {
        Candidato candidato = candidatoRepository.findByIdAndStatus(candidatoId, Status.ATIVO)
            .orElseThrow(() -> new ObjectNotFoundException("Candidato não encontrado"));

        Iterable<Vaga> vagasIterable = vagaRepository.findAllByStatus(Status.ATIVO);
        List<VagaMatch> matches = new ArrayList<>();

        // Convert Iterable to List
        List<Vaga> vagas = new ArrayList<>();
        vagasIterable.forEach(vagas::add);

        for (Vaga vaga : vagas) {
            int pontuacao = calcularPontuacao(vaga, candidato);
            Map<String, String> detalhes = calcularDetalhesCompatibilidade(vaga, candidato);

            if (pontuacao > 0) {
                matches.add(new VagaMatch(vaga, pontuacao, detalhes));
            }
        }

        // Ordenar por pontuação (do maior para o menor)
        matches.sort(Comparator.comparing(VagaMatch::getPontuacao).reversed());

        return matches;
    }

    /**
     * Calcula a pontuação de compatibilidade entre vaga e candidato
     */
    private int calcularPontuacao(Vaga vaga, Candidato candidato) {
        int pontuacao = 0;
        boolean atendeTodosObrigatorios = true;

        // Verificar requisitos
        for (Requisito requisito : vaga.getRequisitos()) {
            boolean atende = false;

            // Verificar se o candidato possui a habilidade requerida
            for (Habilidade habilidade : candidato.getHabilidades()) {
                if (habilidade.getNome().equalsIgnoreCase(requisito.getNome())) {
                    if (habilidade.getNivel() >= requisito.getNivelMinimo()) {
                        atende = true;
                        pontuacao += 10 + (habilidade.getNivel() - requisito.getNivelMinimo()) * 2;
                    }
                    break;
                }
            }

            // Se não atende a um requisito obrigatório, marca a flag
            if (!atende && requisito.getObrigatorio()) {
                atendeTodosObrigatorios = false;
            }
        }

        // Se não atende a todos os requisitos obrigatórios, retorna 0
        if (!atendeTodosObrigatorios) {
            return 0;
        }

        // Verificar compatibilidade de modalidade de trabalho
        if (vaga.getModalidadeTrabalho() != null && candidato.getModalidadePreferida() != null &&
            vaga.getModalidadeTrabalho().equals(candidato.getModalidadePreferida())) {
            pontuacao += 5;
        }

        // Verificar compatibilidade salarial
        if (candidato.getPretensaoSalarial() != null && vaga.getSalario() != null) {
            BigDecimal pretensao = candidato.getPretensaoSalarial();
            BigDecimal salarioVaga = vaga.getSalario();

            // Se o salário da vaga é maior ou igual à pretensão do candidato
            if (salarioVaga.compareTo(pretensao) >= 0) {
                pontuacao += 5;
            } else {
                // Se o salário da vaga é até 10% menor que a pretensão
                BigDecimal diferenca = pretensao.subtract(salarioVaga);
                BigDecimal percentual = diferenca.divide(pretensao, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));

                if (percentual.compareTo(new BigDecimal("10")) <= 0) {
                    pontuacao += 2;
                }
            }
        }

        return pontuacao;
    }

    /**
     * Calcula detalhes de compatibilidade entre vaga e candidato
     */
    private Map<String, String> calcularDetalhesCompatibilidade(Vaga vaga, Candidato candidato) {
        Map<String, String> detalhes = new HashMap<>();

        // Verificar requisitos
        List<String> requisitosAtendidos = new ArrayList<>();
        List<String> requisitosNaoAtendidos = new ArrayList<>();

        for (Requisito requisito : vaga.getRequisitos()) {
            boolean atende = false;

            // Verificar se o candidato possui a habilidade requerida
            for (Habilidade habilidade : candidato.getHabilidades()) {
                if (habilidade.getNome().equalsIgnoreCase(requisito.getNome())) {
                    if (habilidade.getNivel() >= requisito.getNivelMinimo()) {
                        atende = true;
                        requisitosAtendidos.add(requisito.getNome() + " (Nível " + habilidade.getNivel() + ")");
                    } else {
                        requisitosNaoAtendidos.add(requisito.getNome() + " (Nível requerido: " + requisito.getNivelMinimo() + 
                                                  ", Nível do candidato: " + habilidade.getNivel() + ")");
                    }
                    break;
                }
            }

            if (!atende && !requisitosNaoAtendidos.contains(requisito.getNome())) {
                requisitosNaoAtendidos.add(requisito.getNome() + " (Não possui)");
            }
        }

        detalhes.put("requisitosAtendidos", String.join(", ", requisitosAtendidos));
        detalhes.put("requisitosNaoAtendidos", String.join(", ", requisitosNaoAtendidos));

        // Verificar compatibilidade de modalidade de trabalho
        if (vaga.getModalidadeTrabalho() != null && candidato.getModalidadePreferida() != null) {
            if (vaga.getModalidadeTrabalho().equals(candidato.getModalidadePreferida())) {
                detalhes.put("modalidade", "Compatível (" + vaga.getModalidadeTrabalho() + ")");
            } else {
                detalhes.put("modalidade", "Incompatível (Vaga: " + vaga.getModalidadeTrabalho() + 
                                          ", Candidato: " + candidato.getModalidadePreferida() + ")");
            }
        } else {
            // Adicionar mensagem padrão quando modalidade de trabalho não está disponível
            String mensagem = "Informação não disponível";
            if (vaga.getModalidadeTrabalho() != null) {
                mensagem = "Vaga: " + vaga.getModalidadeTrabalho() + ", Preferência do candidato: Não informada";
            } else if (candidato.getModalidadePreferida() != null) {
                mensagem = "Vaga: Não informada, Preferência do candidato: " + candidato.getModalidadePreferida();
            }
            detalhes.put("modalidade", mensagem);
        }

        // Verificar compatibilidade salarial
        if (candidato.getPretensaoSalarial() != null && vaga.getSalario() != null) {
            BigDecimal pretensao = candidato.getPretensaoSalarial();
            BigDecimal salarioVaga = vaga.getSalario();

            if (salarioVaga.compareTo(pretensao) >= 0) {
                detalhes.put("salario", "Compatível (Vaga: R$ " + salarioVaga + 
                                       ", Pretensão: R$ " + pretensao + ")");
            } else {
                detalhes.put("salario", "Incompatível (Vaga: R$ " + salarioVaga + 
                                       ", Pretensão: R$ " + pretensao + ")");
            }
        } else {
            // Adicionar mensagem padrão quando pretensão salarial ou salário da vaga não estão disponíveis
            String mensagem = "Informação não disponível";
            if (vaga.getSalario() != null) {
                mensagem = "Vaga: R$ " + vaga.getSalario() + ", Pretensão: Não informada";
            } else if (candidato.getPretensaoSalarial() != null) {
                mensagem = "Vaga: Não informado, Pretensão: R$ " + candidato.getPretensaoSalarial();
            }
            detalhes.put("salario", mensagem);
        }

        return detalhes;
    }

    // Classes auxiliares para retornar resultados de matching
    public static class CandidatoMatch {
        private Candidato candidato;
        private int pontuacao;
        private Map<String, String> detalhesCompatibilidade;

        public CandidatoMatch(Candidato candidato, int pontuacao, Map<String, String> detalhesCompatibilidade) {
            this.candidato = candidato;
            this.pontuacao = pontuacao;
            this.detalhesCompatibilidade = detalhesCompatibilidade;
        }

        public Candidato getCandidato() {
            return candidato;
        }

        public int getPontuacao() {
            return pontuacao;
        }

        public Map<String, String> getDetalhesCompatibilidade() {
            return detalhesCompatibilidade;
        }
    }

    public static class VagaMatch {
        private Vaga vaga;
        private int pontuacao;
        private Map<String, String> detalhesCompatibilidade;

        public VagaMatch(Vaga vaga, int pontuacao, Map<String, String> detalhesCompatibilidade) {
            this.vaga = vaga;
            this.pontuacao = pontuacao;
            this.detalhesCompatibilidade = detalhesCompatibilidade;
        }

        public Vaga getVaga() {
            return vaga;
        }

        public int getPontuacao() {
            return pontuacao;
        }

        public Map<String, String> getDetalhesCompatibilidade() {
            return detalhesCompatibilidade;
        }
    }
}
