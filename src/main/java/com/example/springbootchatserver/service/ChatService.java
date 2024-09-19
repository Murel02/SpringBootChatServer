package com.example.springbootchatserver.service;

import com.example.springbootchatserver.model.ChatMessage;
import com.example.springbootchatserver.server.ChatServer;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class ChatService {

    private final ChatServer chatServer;

    public ChatService() throws IOException {
        // Initialize ChatServer on a separate thread
        this.chatServer = new ChatServer(5000);
        new Thread(() -> chatServer.start()).start();
    }

    public void handleMessage(ChatMessage message){
        System.out.println("Processing message: " + message);
        chatServer.broadcastMessage(message.toString());
    }
}
