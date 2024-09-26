package com.example.springbootchatserver.config;

// Importerer nødvendige klasser
import com.example.springbootchatserver.server.ChatServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// Angiver, at denne klasse er en konfigurationsklasse i Spring
public class ChatServerConfig {

    @Bean
    // Opretter en ChatServer-bean, som kan injiceres i andre klasser
    public ChatServer chatServer(){
        return new ChatServer(); // Returnerer en ny instans af ChatServer
    }
}
