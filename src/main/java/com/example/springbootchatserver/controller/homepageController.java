package com.example.springbootchatserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Angiver, at denne klasse er en controller
public class HomepageController {

    @GetMapping("/")
    public String rootRedirect(){
        return "redirect:/home";
    }

    @GetMapping("/home") // Kortlægger root URL til homepage.html
    public String homepage() {
        return "homepage"; // Returnerer navnet på HTML-filen uden .html
    }

    @GetMapping("/support") // Kortlægger /support URL til support.html
    public String support() {
        return "support"; // Returnerer navnet på HTML-filen uden .html
    }
}
