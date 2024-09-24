package com.example.springbootchatserver.service;

import com.example.springbootchatserver.server.ChatServer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
public class ChatService {


    @Autowired
    private ChatServer chatServer;

    @Value("${chat.server.port}")
    private int port;


    @PostConstruct
    public void init(){
        new Thread(() -> chatServer.startServer(port)).start();
    }


    public void sendMessageToServer(String message){
        chatServer.broadcastMessage(message);
    }


    public void listenForIncomingMessages(SseEmitter emitter) {
        chatServer.onMessageReceived(message -> {
            try {
                System.out.println("Sending message to emitter: " + message);
                emitter.send(SseEmitter.event().data(message));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
    }

}
