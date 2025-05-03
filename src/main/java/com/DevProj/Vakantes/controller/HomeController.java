package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.model.usuario.Usuario;
import com.DevProj.Vakantes.service.CookieService;
import com.DevProj.Vakantes.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("home/index")
    public String home() {
        return "home/index";
    }

    @GetMapping("/")
    public String redirectHome() {
        return "redirect:http://localhost:3000/home/index";
    }

    @GetMapping("/home")
    public String redirectHome1() {
        return "redirect:http://localhost:3000/home/index";
    }

}
