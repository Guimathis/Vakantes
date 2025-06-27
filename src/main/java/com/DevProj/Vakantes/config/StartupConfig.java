package com.DevProj.Vakantes.config;

import com.DevProj.Vakantes.model.candidato.Candidato;
import com.DevProj.Vakantes.model.candidato.Experiencia;
import com.DevProj.Vakantes.model.candidato.Formacao;
import com.DevProj.Vakantes.model.candidato.Habilidade;
import com.DevProj.Vakantes.model.empresa.Cliente;
import com.DevProj.Vakantes.model.empresa.enums.TipoPessoa;
import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.model.usuario.enums.UserRole;
import com.DevProj.Vakantes.model.util.Contato;
import com.DevProj.Vakantes.model.util.Endereco;
import com.DevProj.Vakantes.model.util.enums.Status;
import com.DevProj.Vakantes.model.vaga.Requisito;
import com.DevProj.Vakantes.model.vaga.Vaga;
import com.DevProj.Vakantes.model.vaga.enums.StatusProcesso;
import com.DevProj.Vakantes.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class StartupConfig {

    @Bean
    public CommandLineRunner initAdminUser(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                                           ClienteRepository clienteRepository,
                                           VagaRepository vagaRepository, RequisitoRepository requisitoRepository, CandidatoRepository candidatoRepository,
                                           HabilidadeRepository habilidadeRepository,
                                           ExperienciaRepository experienciaRepository,
                                           FormacaoRepository formacaoRepository) {
        return args -> {

            Usuario admin = usuarioRepository.findByEmail("vakantesvagas@gmail.com");
            Usuario usuario = usuarioRepository.findByEmail("user@vaka.com");

            if (admin == null && usuario == null) {
                // Criando usuário padrão ADMIN
                Usuario userAdm = new Usuario();
                Contato contatoAdm = new Contato("(67) 99999-9999", "vakantesvagas@gmail.com");
                userAdm.setContato(contatoAdm);
                userAdm.setNomeCompleto("Administrador");
                userAdm.setEmail("vakantesvagas@gmail.com");
                userAdm.setSenha(passwordEncoder.encode("admin")); // Criptografando a senha
                userAdm.setUserRole(UserRole.ADMIN);
                userAdm.setStatus(Status.ATIVO);

                usuarioRepository.save(userAdm);
                System.out.println("Usuário ADMIN criado com sucesso!");

                // Criando usuário padrão ADMIN
                Usuario user = new Usuario();
                Contato contato = new Contato("(67) 99999-9999", "vakantesvagas@gmail.com");
                user.setContato(contato);
                user.setNomeCompleto("Usuario");
                user.setEmail("user@vaka.com");
                user.setSenha(passwordEncoder.encode("admin")); // Criptografando a senha
                user.setUserRole(UserRole.USER);
                user.setStatus(Status.ATIVO);

                usuarioRepository.save(user);

                System.out.println("Usuário USER criado com sucesso!");
                Cliente c1 = new Cliente("Empresa Alpha", TipoPessoa.JURIDICA, "12.345.678/0001-90", Status.ATIVO);
                Contato contato1 = new Contato("(11) 99999-1111", "contato@alpha.com");
                Endereco end1 = new Endereco(c1, "Rua A", "100", "Centro", "São Paulo", "SP", "01000-000");
                c1.setContato(contato1);
                c1.setEndereco(end1);
                clienteRepository.save(c1);

                Cliente c2 = new Cliente("Empresa Beta", TipoPessoa.JURIDICA, "98.765.432/0001-10", Status.ATIVO);
                Contato contato2 = new Contato("(21) 98888-2222", "contato@beta.com");
                Endereco end2 = new Endereco(c2, "Rua B", "200", "Jardins", "Rio de Janeiro", "RJ", "20000-000");
                c2.setContato(contato2);
                c2.setEndereco(end2);
                clienteRepository.save(c2);

                Cliente c3 = new Cliente("João Silva", TipoPessoa.FISICA, "123.456.789-00", Status.ATIVO);
                Contato contato3 = new Contato("(31) 97777-3333", "joao.silva@email.com");
                Endereco end3 = new Endereco(c3, "Rua C", "300", "Boa Vista", "Belo Horizonte", "MG", "30000-000");
                c3.setContato(contato3);
                c3.setEndereco(end3);
                clienteRepository.save(c3);

                Cliente c4 = new Cliente("Maria Souza", TipoPessoa.FISICA, "987.654.321-00", Status.ATIVO);
                Contato contato4 = new Contato("(21) 96666-4444", "maria.souza@email.com");
                Endereco end4 = new Endereco(c4, "Rua D", "400", "Copacabana", "Rio de Janeiro", "RJ", "22000-000");
                c4.setContato(contato4);
                c4.setEndereco(end4);
                clienteRepository.save(c4);

                Cliente c5 = new Cliente("Empresa Gama", TipoPessoa.JURIDICA, "11.222.333/0001-44", Status.ATIVO);
                Contato contato5 = new Contato("(31) 95555-5555", "contato@gama.com");
                Endereco end5 = new Endereco(c5, "Rua E", "500", "Savassi", "Belo Horizonte", "MG", "30100-000");
                c5.setContato(contato5);
                c5.setEndereco(end5);
                clienteRepository.save(c5);

                // Vagas
                Vaga v1 = vagaRepository.save(new Vaga("Desenvolvedor Java", "Desenvolvimento backend", LocalDate.now(), 8000.00, "Pleno", "CLT", "Remoto", "São Paulo/SP", c1, Status.ATIVO, StatusProcesso.ABERTA));
                Vaga v2 = vagaRepository.save(new Vaga("Analista de Dados", "Análise de dados e BI", LocalDate.now(), 7000.00, "Sênior", "PJ", "Híbrido", "Rio de Janeiro/RJ", c2, Status.ATIVO, StatusProcesso.ABERTA));
                Vaga v3 = vagaRepository.save(new Vaga("Dev Frontend", "React e Angular", LocalDate.now(), 6500.00, "Júnior", "CLT", "Presencial", "Belo Horizonte/MG", c3, Status.ATIVO, StatusProcesso.ABERTA));
                Vaga v4 = vagaRepository.save(new Vaga("QA Tester", "Testes automatizados", LocalDate.now(), 6000.00, "Pleno", "CLT", "Remoto", "São Paulo/SP", c1, Status.ATIVO, StatusProcesso.ABERTA));
                Vaga v5 = vagaRepository.save(new Vaga("Product Owner", "Gestão de produto", LocalDate.now(), 9000.00, "Sênior", "PJ", "Híbrido", "Rio de Janeiro/RJ", c2, Status.ATIVO, StatusProcesso.ABERTA));
                Vaga v6 = vagaRepository.save(new Vaga("UX Designer", "Design de experiência", LocalDate.now(), 7500.00, "Pleno", "CLT", "Presencial", "Belo Horizonte/MG", c3, Status.ATIVO, StatusProcesso.ABERTA));
                Vaga v7 = vagaRepository.save(new Vaga("DevOps", "Infraestrutura e CI/CD", LocalDate.now(), 8500.00, "Sênior", "PJ", "Remoto", "São Paulo/SP", c4, Status.ATIVO, StatusProcesso.ABERTA));
                Vaga v8 = vagaRepository.save(new Vaga("Scrum Master", "Agile Coach", LocalDate.now(), 8000.00, "Pleno", "CLT", "Híbrido", "Rio de Janeiro/RJ", c5, Status.ATIVO, StatusProcesso.ABERTA));
                Vaga v9 = vagaRepository.save(new Vaga("Desenvolvedor Mobile", "Android/iOS", LocalDate.now(), 7000.00, "Júnior", "CLT", "Presencial", "Belo Horizonte/MG", c5, Status.ATIVO, StatusProcesso.ABERTA));
                Vaga v10 = vagaRepository.save(new Vaga("Analista de Suporte", "Suporte técnico", LocalDate.now(), 5000.00, "Pleno", "CLT", "Remoto", "São Paulo/SP", c4, Status.ATIVO, StatusProcesso.ABERTA));

                // Requisitos para vaga 1 (Desenvolvedor Java)
                requisitoRepository.save(new Requisito("Java", 4, true, v1));
                requisitoRepository.save(new Requisito("Spring Boot", 3, true, v1));
                requisitoRepository.save(new Requisito("SQL", 2, false, v1));

                // Requisitos para vaga 2 (Analista de Dados)
                requisitoRepository.save(new Requisito("Power BI", 3, true, v2));
                requisitoRepository.save(new Requisito("SQL", 4, true, v2));
                requisitoRepository.save(new Requisito("Python", 2, false, v2));

                // Requisitos para vaga 3 (Dev Frontend)
                requisitoRepository.save(new Requisito("React", 3, true, v3));
                requisitoRepository.save(new Requisito("Angular", 2, false, v3));

                // Requisitos para vaga 4 (QA Tester)
                requisitoRepository.save(new Requisito("Selenium", 3, true, v4));
                requisitoRepository.save(new Requisito("JUnit", 2, false, v4));

                // Requisitos para vaga 5 (Product Owner)
                requisitoRepository.save(new Requisito("Scrum", 4, true, v5));
                requisitoRepository.save(new Requisito("Gestão de Projetos", 3, true, v5));
                requisitoRepository.save(new Requisito("Kanban", 2, false, v5));

                // Requisitos para vaga 6 (UX Designer)
                requisitoRepository.save(new Requisito("Figma", 3, true, v6));
                requisitoRepository.save(new Requisito("UX Research", 2, false, v6));

                // Requisitos para vaga 7 (DevOps)
                requisitoRepository.save(new Requisito("Docker", 3, true, v7));
                requisitoRepository.save(new Requisito("CI/CD", 3, true, v7));
                requisitoRepository.save(new Requisito("AWS", 2, false, v7));

                // Requisitos para vaga 8 (Scrum Master)
                requisitoRepository.save(new Requisito("Scrum", 4, true, v8));
                requisitoRepository.save(new Requisito("Facilitação", 3, false, v8));

                // Requisitos para vaga 9 (Desenvolvedor Mobile)
                requisitoRepository.save(new Requisito("Android", 3, true, v9));
                requisitoRepository.save(new Requisito("iOS", 2, false, v9));
                requisitoRepository.save(new Requisito("Flutter", 2, false, v9));

                // Requisitos para vaga 10 (Analista de Suporte)
                requisitoRepository.save(new Requisito("Atendimento", 3, true, v10));
                requisitoRepository.save(new Requisito("Redes", 2, false, v10));

                // Candidato 1
                Candidato cand1 = new Candidato("1234567", "123.456.789-01", "Carlos Pereira", "1990-05-10",
                        new Contato("(11) 99999-1111", "carlos@email.com"),
                        new Endereco("Rua A", "100", "Centro", "São Paulo", "SP", "01000-000"));
                candidatoRepository.save(cand1);

                habilidadeRepository.save(new Habilidade("Java", 4, cand1));
                habilidadeRepository.save(new Habilidade("Spring Boot", 3, cand1));
                experienciaRepository.save(new Experiencia("Empresa X", "Desenvolvedor", "Backend Java",
                        LocalDate.of(2018, 1, 1), LocalDate.of(2021, 12, 31), false, cand1));
                experienciaRepository.save(new Experiencia("Empresa Y", "Analista", "Suporte",
                        LocalDate.of(2022, 1, 1), null, true, cand1));
                formacaoRepository.save(new Formacao("USP", "Ciência da Computação", "Graduação",
                        LocalDate.of(2010, 2, 1), LocalDate.of(2014, 12, 1), true, cand1));

                // Candidato 2
                Candidato cand2 = new Candidato("2345678", "234.567.890-12", "Fernanda Lima", "1992-08-15",
                        new Contato("(21) 98888-2222", "fernanda@email.com"),
                        new Endereco("Rua B", "200", "Jardins", "Rio de Janeiro", "RJ", "20000-000"));
                candidatoRepository.save(cand2);

                habilidadeRepository.save(new Habilidade("Power BI", 4, cand2));
                habilidadeRepository.save(new Habilidade("SQL", 5, cand2));
                experienciaRepository.save(new Experiencia("Empresa Z", "Analista de Dados", "BI e Analytics",
                        LocalDate.of(2017, 3, 1), null, true, cand2));
                formacaoRepository.save(new Formacao("PUC-Rio", "Engenharia de Produção", "Graduação",
                        LocalDate.of(2011, 2, 1), LocalDate.of(2015, 12, 1), true, cand2));

                // Candidato 3
                Candidato cand3 = new Candidato("3456789", "345.678.901-23", "Ricardo Alves", "1988-11-20",
                        new Contato("(31) 97777-3333", "ricardo@email.com"),
                        new Endereco("Rua C", "300", "Boa Vista", "Belo Horizonte", "MG", "30000-000"));
                candidatoRepository.save(cand3);

                habilidadeRepository.save(new Habilidade("React", 3, cand3));
                habilidadeRepository.save(new Habilidade("Angular", 2, cand3));
                experienciaRepository.save(new Experiencia("Startup A", "Frontend", "Web",
                        LocalDate.of(2019, 6, 1), null, true, cand3));
                formacaoRepository.save(new Formacao("UFMG", "Sistemas de Informação", "Graduação",
                        LocalDate.of(2008, 2, 1), LocalDate.of(2012, 12, 1), true, cand3));

                // Candidato 4
                Candidato cand4 = new Candidato("4567890", "456.789.012-34", "Juliana Costa", "1995-03-22",
                        new Contato("(21) 96666-4444", "juliana@email.com"),
                        new Endereco("Rua D", "400", "Copacabana", "Rio de Janeiro", "RJ", "22000-000"));
                candidatoRepository.save(cand4);

                habilidadeRepository.save(new Habilidade("Selenium", 4, cand4));
                habilidadeRepository.save(new Habilidade("JUnit", 3, cand4));
                experienciaRepository.save(new Experiencia("Empresa B", "QA Tester", "Testes automatizados",
                        LocalDate.of(2020, 1, 1), null, true, cand4));
                formacaoRepository.save(new Formacao("UFRJ", "Engenharia de Software", "Graduação",
                        LocalDate.of(2013, 2, 1), LocalDate.of(2017, 12, 1), true, cand4));

                // Candidato 5
                Candidato cand5 = new Candidato("5678901", "567.890.123-45", "Mariana Souza", "1993-07-30",
                        new Contato("(31) 95555-5555", "mariana@email.com"),
                        new Endereco("Rua E", "500", "Savassi", "Belo Horizonte", "MG", "30100-000"));
                candidatoRepository.save(cand5);

                habilidadeRepository.save(new Habilidade("Android", 3, cand5));
                habilidadeRepository.save(new Habilidade("iOS", 2, cand5));
                experienciaRepository.save(new Experiencia("Empresa C", "Mobile Dev", "Apps Android/iOS",
                        LocalDate.of(2016, 5, 1), LocalDate.of(2020, 12, 1), false, cand5));
                experienciaRepository.save(new Experiencia("Empresa D", "Mobile Lead", "Liderança",
                        LocalDate.of(2021, 1, 1), null, true, cand5));
                formacaoRepository.save(new Formacao("UNI-BH", "Análise e Desenvolvimento de Sistemas", "Tecnólogo",
                        LocalDate.of(2012, 2, 1), LocalDate.of(2014, 12, 1), true, cand5));
            }
        };
    }
}