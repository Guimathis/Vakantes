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

@Service
public class MatchingService {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private CandidatoRepository candidatoRepository;

    /**
     * 
     * Retorna candidatos já cadastrados na vaga com sua pontuação
     */
    public List<CandidatoMatch> avaliarCandidatosVaga(Long vagaId) {
        Vaga vaga = vagaRepository.findByCodigoAndStatus(vagaId, Status.ATIVO)
            .orElseThrow(() -> new ObjectNotFoundException("Vaga não encontrada"));

        List<CandidatoMatch> matches = new ArrayList<>();

        // Usar apenas os candidatos já cadastrados na vaga
        List<Candidato> candidatosJaCadastrados = vaga.getCandidatos();

        for (Candidato candidato : candidatosJaCadastrados) {
            // Verificar se o candidato está ativo
            if (candidato.getStatus() == Status.ATIVO) {
                int pontuacao = calcularPontuacao(vaga, candidato);
                Map<String, String> detalhes = calcularDetalhesCompatibilidade(vaga, candidato);

                // Adicionar o candidato à lista de matches independentemente da pontuação
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
        int requisitosObrigatoriosTotal = 0;
        int requisitosObrigatoriosAtendidos = 0;

        // Verificar requisitos
        for (Requisito requisito : vaga.getRequisitos()) {
            boolean atende = false;

            // Contar requisitos obrigatórios
            if (requisito.getObrigatorio()) {
                requisitosObrigatoriosTotal++;
            }

            // Verificar se o candidato possui a habilidade requerida
            for (Habilidade habilidade : candidato.getHabilidades()) {
                if (habilidade.getNome().equalsIgnoreCase(requisito.getNome())) {
                    // Pontuação parcial mesmo se não atinge o nível mínimo
                    if (habilidade.getNivel() >= requisito.getNivelMinimo()) {
                        atende = true;
                        // Pontuação completa para habilidade que atende o nível
                        pontuacao += 10 + (habilidade.getNivel() - requisito.getNivelMinimo()) * 2;
                    } else {
                        // Pontuação parcial para habilidade abaixo do nível requerido
                        // Quanto mais próximo do nível requerido, mais pontos
                        int diferenca = requisito.getNivelMinimo() - habilidade.getNivel();
                        pontuacao += Math.max(0, 5 - diferenca); // 5 pontos para diferença de 0, 4 para 1, etc.
                    }
                    break;
                }
            }

            // Contabilizar requisitos obrigatórios atendidos
            if (atende && requisito.getObrigatorio()) {
                requisitosObrigatoriosAtendidos++;
            }
        }

        // Bônus para requisitos obrigatórios atendidos
        if (requisitosObrigatoriosTotal > 0) {
            // Percentual de requisitos obrigatórios atendidos (0 a 100)
            int percentualAtendido = (requisitosObrigatoriosAtendidos * 100) / requisitosObrigatoriosTotal;
            // Bônus de até 20 pontos baseado no percentual de requisitos obrigatórios atendidos
            pontuacao += (percentualAtendido * 20) / 100;
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
        List<String> requisitosAtendidosParcialmente = new ArrayList<>();
        List<String> requisitosNaoAtendidos = new ArrayList<>();

        for (Requisito requisito : vaga.getRequisitos()) {
            boolean atende = false;
            boolean atendeParcialmente = false;

            // Verificar se o candidato possui a habilidade requerida
            for (Habilidade habilidade : candidato.getHabilidades()) {
                if (habilidade.getNome().equalsIgnoreCase(requisito.getNome())) {
                    if (habilidade.getNivel() >= requisito.getNivelMinimo()) {
                        atende = true;
                        requisitosAtendidos.add(requisito.getNome() + " (Nível " + habilidade.getNivel() + ")");
                    } else {
                        atendeParcialmente = true;
                        int diferenca = requisito.getNivelMinimo() - habilidade.getNivel();
                        requisitosAtendidosParcialmente.add(requisito.getNome() + " (Nível requerido: " + requisito.getNivelMinimo() + 
                                                  ", Nível do candidato: " + habilidade.getNivel() + 
                                                  ", Pontuação parcial)");
                    }
                    break;
                }
            }

            if (!atende && !atendeParcialmente) {
                requisitosNaoAtendidos.add(requisito.getNome() + " (Não possui)");
            }
        }

        detalhes.put("requisitosAtendidos", String.join(", ", requisitosAtendidos));
        detalhes.put("requisitosAtendidosParcialmente", String.join(", ", requisitosAtendidosParcialmente));
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
