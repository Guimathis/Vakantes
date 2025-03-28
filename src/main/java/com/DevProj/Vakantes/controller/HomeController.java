package com.DevProj.Vakantes.controller;

import com.DevProj.Vakantes.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("home/index")
    public String home(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        model.addAttribute("nomeUsuario", CookieService.getCookie(request, "nomeUsuario"));
        model.addAttribute("idUsuario", CookieService.getCookie(request, "usuarioId"));
        return "home/index";  // Deve existir um arquivo home.html
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