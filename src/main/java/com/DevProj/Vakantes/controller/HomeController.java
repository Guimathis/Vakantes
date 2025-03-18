package com.DevProj.Vakantes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("home/index")
    public String home() {
        return "home/index";  // Deve existir um arquivo home.html
    }

    @GetMapping("/")
    public String redirectToNewUrl() {
        return "redirect:http://localhost:3000/home/index";
    }

}