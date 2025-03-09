package com.vakantes.Vakantes.auth.service;

import java.sql.Date;

import com.vakantes.Vakantes.model.Usuario;
import com.vakantes.Vakantes.model.dtos.AuthenticationDto;
import com.vakantes.Vakantes.model.dtos.LoginResponseDto;
import com.vakantes.Vakantes.model.dtos.RegisterDto;
import com.vakantes.Vakantes.repositories.UsuarioRepository;
import com.vakantes.Vakantes.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@Service
public class AuthorizationService implements UserDetailsService{
    @Autowired
    private ApplicationContext context;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByLogin(email);
    }

    public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDto data){
        AuthenticationManager authenticationManager = context.getBean(AuthenticationManager.class);

        var loginSenha = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
        var auth = authenticationManager.authenticate(loginSenha);
        var token = tokenService.generateToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    public ResponseEntity<Object> register (@RequestBody RegisterDto registerDto){
        if (this.usuarioRepository.findByLogin(registerDto.login()) != null )
            return ResponseEntity.badRequest().build();
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDto.senha());

        Usuario newUser = new Usuario(registerDto.login(), encryptedPassword, registerDto.role());
        newUser.setCreatedAt(new Date(System.currentTimeMillis()));
        this.usuarioRepository.save(newUser);
        return ResponseEntity.ok().build();
    }

}