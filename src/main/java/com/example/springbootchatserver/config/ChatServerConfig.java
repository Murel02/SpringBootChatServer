package com.example.springbootchatserver.config;

import com.example.springbootchatserver.server.ChatServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatServerConfig {

    @Bean
    public ChatServer chatServer(){
        return new ChatServer();
    }
}
