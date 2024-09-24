package com.example.springbootchatserver.service;

import com.example.springbootchatserver.model.ChatMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ChatService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final List<ChatMessage> chatHistory = new ArrayList<>();

    // Method to retrieve the chat history
    public List<ChatMessage> getChatMessages() {
        return new ArrayList<>(chatHistory);
    }

    // Method to listen for incoming messages
    public void listenForIncomingMessages(SseEmitter emitter) {
        emitters.add(emitter);

        // Handle emitter lifecycle events
        emitter.onCompletion(() -> {
            emitters.remove(emitter);
            System.out.println("Emitter completed and removed.");
        });

        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            System.out.println("Emitter timed out and removed.");
        });

        emitter.onError(e -> {
            emitters.remove(emitter);
            System.err.println("Error with emitter: " + e.getMessage());
        });
    }

    // Method to broadcast a message to all clients
    public void broadcastMessage(ChatMessage message) {
        chatHistory.add(message); // Add the message to chat history
        List<SseEmitter> deadEmitters = new ArrayList<>();

        // Get the formatted message string
        String formattedMessage = message.toString();

        // Iterate over a copy of the emitters list to avoid ConcurrentModificationException
        for (SseEmitter emitter : emitters) {
            try {
                // Send the formatted message string to each connected client
                emitter.send(SseEmitter.event().data(formattedMessage));
            } catch (IOException e) {
                deadEmitters.add(emitter); // Mark emitter for removal if an error occurs
                System.err.println("Error broadcasting message to client: " + e.getMessage());
            }
        }

        // Remove the dead emitters
        emitters.removeAll(deadEmitters);
    }
}
