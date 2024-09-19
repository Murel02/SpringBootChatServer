package com.example.springbootchatserver.service;

import com.example.springbootchatserver.client.ChatClient;
import com.example.springbootchatserver.model.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ChatService {

    private final Set<ChatClient> clients = new HashSet<>();

    public void registerClient(ChatClient client){
        clients.add(client);
    }

    public void unregisterClient(ChatClient client){
        clients.remove(client);
    }

    public void handleMessage(ChatMessage message){
        System.out.println("Processing message: " + message);

        broadcastMessage(message);
    }

    public void broadcastMessage(ChatMessage message){
        for (ChatClient client : clients){
            client.sendMessage(message.toString());
        }
    }
}
