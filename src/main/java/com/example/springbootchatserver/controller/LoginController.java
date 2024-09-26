package com.example.springbootchatserver.controller;

// Importerer nødvendige klasser
import com.example.springbootchatserver.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
// Angiver, at denne klasse er en controller for login-funktionalitet
public class LoginController {

    private final UserService userService;

    @Autowired
    // Konstruktor, som automatisk injicerer UserService for at kunne bruge autentificerings- og registreringsmetoder
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    // Håndterer GET-forespørgsler til login-siden
    public String loginPage() {
        return "login"; // Returnerer login-siden
    }

    // Håndterer login-forespørgsler
    @PostMapping("/loginRequest")
    public String login(@RequestParam("username") String username,  // Henter brugernavn fra formularen
                        @RequestParam("password") String password,  // Henter adgangskode fra formularen
                        HttpSession session,  // Session bruges til at gemme brugeroplysninger
                        Model model) {  // Model bruges til at sende data til HTML-sider

        // Tjekker om brugernavn og adgangskode er gyldige ved at kalde authenticate-metoden fra userService
        if (userService.authenticate(username, password)) {
            // Hvis autentificeringen er succesfuld, gemmes brugernavnet i sessionen
            session.setAttribute("username", username);
            return "redirect:/chat"; // Omdirigerer til chat-siden ved succesfuldt login
        } else {
            // Hvis login fejler, tilføjes en fejlbesked til modellen
            model.addAttribute("error", "Ugyldigt brugernavn eller adgangskode");
            return "/login"; // Returnerer til login-siden ved fejl
        }
    }

    @GetMapping("/register")
    // Håndterer GET-forespørgsler til registreringssiden
    public String registerPage() {
        return "registeruser"; // Returnerer registreringssiden
    }

    // Håndterer registreringsforespørgsler
    @PostMapping("/registeruser")
    public String registeruser(@RequestParam("username") String username,  // Henter brugernavn fra formularen
                               @RequestParam("password") String password,  // Henter adgangskode fra formularen
                               Model model) {  // Model bruges til at sende data til HTML-sider

        // Tjekker om brugernavnet allerede findes, ved at kalde registerUser-metoden fra userService
        if (userService.registerUser(username, password)) {
            // Hvis registrering lykkes, omdirigeres til login-siden
            return "redirect:/login";
        } else {
            // Hvis registrering fejler, tilføjes en fejlbesked til modellen
            model.addAttribute("error", "Brugernavn findes allerede");
            return "registeruser"; // Returnerer til registreringssiden ved fejl
        }
    }

    @GetMapping("/logout")
    // Håndterer GET-forespørgsler til logout
    public String logout(HttpSession session) {
        session.invalidate(); // Invaliderer sessionen og logger brugeren ud
        return "redirect:/login"; // Omdirigerer til login-siden
    }
}
