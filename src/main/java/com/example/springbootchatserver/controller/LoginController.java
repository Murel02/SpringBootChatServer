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

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    // Handle login requests
    @PostMapping("/loginRequest")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {

        if (userService.authenticate(username, password)) {
            session.setAttribute("username", username);
            return "redirect:/chat"; // Redirect to chat.html upon successful login
         } else {
            model.addAttribute("error", "Ugyldigt brugernavn eller adgangskode");
            return "/login"; // Return to login page on failure
        }
    }

    @GetMapping("/register")
    public String registerPage(){
        return "registeruser";
    }

    //Handle sign up request
    @PostMapping("/registeruser")
    public String registeruser(@RequestParam ("username") String username,
                               @RequestParam ("password") String password,
                               Model model) {


        if (userService.registerUser(username, password)) {
            return "redirect:/login";

        } else {
            model.addAttribute("error", "Username already exists");
            return "registeruser";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }

}
