package com.example.springbootchatserver.controller;

import com.example.springbootchatserver.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session, Model model){

        if (userService.authenticate(username, password)){
            session.setAttribute("username", username);
            return "redirect:/chat";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }
    
    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           Model model){

        if (userService.registerUser(username, password)){
            model.addAttribute("message", "Registration successful! Please log in.");
        } else {
            model.addAttribute("error", "Username already exists.");
        }
        return "login";
    }
}
