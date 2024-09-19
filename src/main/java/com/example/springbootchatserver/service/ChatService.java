package com.example.springbootchatserver.service;

import com.example.springbootchatserver.client.ChatClient;
import com.example.springbootchatserver.model.ChatMessage;
import com.example.springbootchatserver.server.ChatServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private final ChatClient chatClient;

    @Autowired
    public ChatService(ChatClient chatClient){
        this.chatClient = chatClient;
    }

    public void handleMessage(String message){
       try {
           chatClient.sendMessage(message);
           String response = chatClient.receiveMessage();
       }catch (IOException e){
           e.printStackTrace();
       }
    }

    public List<String> getMessages(){
        return new ArrayList<>();
    }
}
