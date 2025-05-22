package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
