package com.example.springbootchatserver.service;

import com.example.springbootchatserver.model.ChatMessage;
import com.example.springbootchatserver.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ChatService {

    // Liste til at gemme alle aktive SseEmitters (til forbindelser med klienter)
    private final ConcurrentHashMap<String, SseEmitter> userEmitters = new ConcurrentHashMap<>();
    // Liste til at gemme chatbeskeder, så de kan hentes senere (chat-historik)
    private final List<ChatMessage> chatHistory = new ArrayList<>();

    // Metode til at hente chat-historikken
    public List<ChatMessage> getChatMessages() {
        return new ArrayList<>(chatHistory); // Returnerer en kopi af chat-historikken
    }

    // Metode til at lytte efter indkommende beskeder
    public void listenForIncomingMessages(String username, SseEmitter emitter) {
        userEmitters.put(username, emitter); // Tilføj en ny emitter til listen over aktive forbindelser

        // Håndter emitter-livscyklushændelser
        emitter.onCompletion(() -> {
            userEmitters.remove(username); // Fjern emitteren, hvis den afsluttes
            System.out.println("Emitter afsluttet og fjernet.");
        });

        emitter.onTimeout(() -> {
            userEmitters.remove(username); // Fjern emitteren, hvis den tidsudløber
            System.out.println("Emitter tidsudløb og fjernet.");
        });

        emitter.onError(e -> {
            userEmitters.remove(username); // Fjern emitteren, hvis der opstår en fejl
            System.err.println("Fejl med emitter: " + e.getMessage());
        });
    }

    // Metode til at sende en besked til alle klienter (broadcast)
    public void broadcastMessage(ChatMessage message) {
        chatHistory.add(message); // Tilføj beskeden til chat-historikken
        List<String> deadEmitters = new ArrayList<>(); // Liste til "døde" emitters

        // Formater beskeden som en streng
        String formattedMessage = message.toString();

        // Iterér over en kopi af emitters-listen for at undgå ConcurrentModificationException
        userEmitters.forEach((username, emitter) -> {
            try {
                // Send the formatted message string to each connected client
                emitter.send(SseEmitter.event().data(formattedMessage));
            } catch (IOException e) {
                deadEmitters.add(username); // Mark emitter for removal if an error occurs
                System.err.println("Error broadcasting message to user " + username + ": " + e.getMessage());
            }
        });


        // Fjern de "døde" emitters fra listen
       deadEmitters.forEach(userEmitters::remove);
    }

    // Metode til at sende en privat besked (unicast) til en specifik bruger
    public void unicastMessage(ChatMessage message, String recipient) {
        chatHistory.add(message); // Optional: Add the message to chat history

        String formattedMessage = message.toString();

        SseEmitter targetEmitter = userEmitters.get(recipient); // Retrieve the emitter for the target user

        if (targetEmitter != null) {
            try {
                // Send the formatted message string to the specific client
                targetEmitter.send(SseEmitter.event().data(formattedMessage));
                System.out.println("Message sent to user " + recipient);
            } catch (IOException e) {
                System.err.println("Error sending unicast message to user " + recipient + ": " + e.getMessage());
                userEmitters.remove(recipient); // Optionally remove the emitter on error
            }
        } else {
            System.err.println("User " + recipient + " not found or not connected.");
        }
    }
}
