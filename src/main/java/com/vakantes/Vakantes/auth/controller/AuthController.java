package com.vakantes.Vakantes.auth.controller;

import com.vakantes.Vakantes.auth.service.AuthorizationService;
import com.vakantes.Vakantes.model.Usuario;
import com.vakantes.Vakantes.model.dtos.RegisterDto;
import com.vakantes.Vakantes.model.dtos.AuthenticationDto;
import com.vakantes.Vakantes.model.enums.UserRole;
import com.vakantes.Vakantes.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;


@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDto authenticationDto) {
        return authorizationService.login(authenticationDto);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterDto registerDto){
        return authorizationService.register(registerDto);
    }
}