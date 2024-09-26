package com.example.springbootchatserver.controller;

// Importerer nødvendige klasser
import com.example.springbootchatserver.model.ChatMessage;
import com.example.springbootchatserver.model.User;
import com.example.springbootchatserver.service.ChatService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
// Angiver, at denne klasse er en controller, der håndterer HTTP-forespørgsler
public class ChatController {

    // Automatisk injektion af chatService for at kunne bruge dens metoder
    private ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    // Håndterer GET-forespørgsler til "/chat"-siden
    public String chatPage(HttpSession session, Model model) {
        // Tjekker om brugeren er logget ind
        String username = (String) session.getAttribute("username");

        if (username == null) {
            // Hvis brugeren ikke er logget ind, sendes de til login-siden
            return "redirect:/login";
        }

        // Henter chatbeskederne fra chatService
        List<ChatMessage> messages = chatService.getChatMessages();
        // Tilføjer beskederne til modellen, så de kan vises i HTML-filen
        model.addAttribute("messages", messages);
        return "chat"; // Returnerer chat-siden
    }

    @GetMapping("/stream")
    // Håndterer GET-forespørgsler til "/stream", som er en Server-Sent Events (SSE) endpoint
    public SseEmitter stream(HttpSession session) {
        // Tjekker om brugeren er logget ind
        String username = (String) session.getAttribute("username");
        if (username == null) {
            // Hvis brugeren ikke er logget ind, kastes der en fejl
            throw new IllegalStateException("User must be logged in to access this endpoint.");
        }

        // Opretter en SseEmitter med en timeout på 1 time (60 * 60 * 1000 ms)
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L);
        // Lytter til indkommende beskeder via chatService
        chatService.listenForIncomingMessages(session.getAttribute("username").toString() ,emitter);
        return emitter; // Returnerer emitteren
    }

    /*
    Metoden kører, når man trykker på send-knappen.
    Den bruger en besked fra en HTML-fil via @RequestParam og et Model-objekt.
    Hvis beskeden ikke er null eller tom, oprettes et ChatMessage-objekt med brugernavnet fra en User-objekt, den aktuelle tid og beskeden fra HTML-filen.
    Brugeren sendes derefter tilbage til chat-siden.
    */
    @PostMapping("/send")
    // Håndterer POST-forespørgsler til "/send" for at sende en besked
    public String sendMessage(@RequestParam("message") String message, @RequestParam(required = false) String recipient, HttpSession session) {
        // Henter brugernavnet fra sessionen
        String username = (String) session.getAttribute("username");

        if (username == null) {
            // Hvis brugeren ikke er logget ind, omdirigeres de til login-siden
            return "redirect:/login";
        }

        // Opretter et tidsstempel med den aktuelle tid
        LocalDateTime timestamp = LocalDateTime.now();
        // Definerer beskedtypen som tekst (TEXT)
        String type = "TEXT";

        // Opretter et nyt ChatMessage-objekt med brugernavn, tidsstempel, type og besked
        ChatMessage newMessage = new ChatMessage(username, timestamp, type, message);

        // Tilføjer beskeden til sessionen for at holde styr på den sidst sendte besked
        session.setAttribute("lastSentMessage", newMessage);
        System.out.println("Modtaget besked: " + message);


        if (recipient == null) {
            // Sender beskeden til alle brugere via chatService
            chatService.broadcastMessage(newMessage);
        }else {
            chatService.unicastMessage(newMessage, recipient);
        }

        return "redirect:/chat"; // Omdirigerer til chat-siden
    }


}
