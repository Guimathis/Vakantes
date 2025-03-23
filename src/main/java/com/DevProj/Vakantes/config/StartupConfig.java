package com.DevProj.Vakantes.config;

import com.DevProj.Vakantes.model.Usuario;
import com.DevProj.Vakantes.model.enums.UserRole;
import com.DevProj.Vakantes.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class StartupConfig {

    @Bean
    public CommandLineRunner initAdminUser(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Usuario admin = usuarioRepository.findByEmail("admin@vaka.com");

            if (admin == null) {
                // Criando usuário padrão ADMIN
                Usuario user = new Usuario();
                user.setNomeCompleto("Administrador");
                user.setEmail("admin@vaka.com");
                user.setSenha(passwordEncoder.encode("admin")); // Criptografando a senha
                user.setUserRole(UserRole.ADMIN);

                usuarioRepository.save(admin);
                System.out.println("Usuário ADMIN criado com sucesso!");
            }

            Usuario usuario = usuarioRepository.findByEmail("user@vaka.com");
            if (usuario == null) {
                // Criando usuário padrão ADMIN
                Usuario user = new Usuario();
                user.setNomeCompleto("Usuario");
                user.setEmail("user@vaka.com");
                user.setSenha(passwordEncoder.encode("user")); // Criptografando a senha
                user.setUserRole(UserRole.USER);

                usuarioRepository.save(user);
                System.out.println("Usuário USER criado com sucesso!");
            }

        };
    }
}