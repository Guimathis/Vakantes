package com.DevProj.Vakantes.config;

import com.DevProj.Vakantes.model.Usuario;
import com.DevProj.Vakantes.model.enums.UserRole;
import com.DevProj.Vakantes.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@Configuration
public class StartupConfig {

    @Bean
    public CommandLineRunner initAdminUser(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verifica se já existe um usuário admin
            Optional<Usuario> adminExists = usuarioRepository.findByLogin("admin");

            if (adminExists.isEmpty()) {
                // Criando usuário padrão ADMIN
                Usuario admin = new Usuario();
                admin.setNomeCompleto("Administrador");
                admin.setLogin("admin");
                admin.setSenha(passwordEncoder.encode("admin")); // Criptografando a senha
                admin.setUserRole(UserRole.ADMIN);

                usuarioRepository.save(admin);
                System.out.println("Usuário ADMIN criado com sucesso!");
            }
        };
    }
}