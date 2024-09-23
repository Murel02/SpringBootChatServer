package com.example.springbootchatserver.controller;

import com.example.springbootchatserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // Marker denne klasse som en Spring MVC Controller, der håndterer HTTP-anmodninger
public class LoginController {

    private final UserService userService; // UserService håndterer logik for brugere (login/registrering)


    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    // Simpel login-side (hvis nødvendigt for visning)
    @GetMapping("/login")
    public String loginPage() {
        return "Login side her"; // En placeholder-tekst, da vi ikke bruger MVC-visninger
    }

    // Håndterer login-anmodninger
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, // Henter brugernavn fra formularen
                        @RequestParam("password") String password, // Henter adgangskode fra formularen
                        Model model) { // Model bruges til at sende data tilbage til visningen

        // Autentificerer brugeren ved hjælp af UserService
        if (userService.authenticate(username, password)) {
            return "Login succesfuldt!"; // Returnerer en succesmeddelelse som svar
        } else {
            model.addAttribute("error", "Ugyldigt brugernavn eller adgangskode"); // Tilføjer fejlbesked til modellen
            return "Login mislykkedes"; // Returnerer en fejlbesked som svar
        }
    }

    // Håndterer registrering af nye brugere
    @PostMapping("/register")
    public String register(@RequestParam("username") String username, // Henter brugernavn fra formularen
                           @RequestParam("password") String password, // Henter adgangskode fra formularen
                           Model model) {

        // Registrerer den nye bruger ved hjælp af UserService
        if (userService.registerUser(username, password)) {
            model.addAttribute("message", "Registrering succesfuld! Log venligst ind.");
            return "Registrering succesfuld!"; // Returnerer en succesmeddelelse som svar
        } else {
            model.addAttribute("error", "Brugernavnet findes allerede.");
            return "Registrering mislykkedes: Brugernavnet findes allerede."; // Returnerer en fejlbesked som svar
        }
    }
}
