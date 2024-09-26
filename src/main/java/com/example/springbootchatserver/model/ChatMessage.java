package com.example.springbootchatserver.model;

// Importerer nødvendige klasser til dato og tid
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage {
    // Variabel til at gemme clientId (brugerens ID)
    private String clientId;
    // Variabel til at gemme tidsstemplet for beskeden
    private LocalDateTime timestamp;
    // Variabel til at gemme beskedtypen (f.eks. tekst, billede)
    private String type;
    // Variabel til at gemme selve beskedens indhold
    private String content;

    // Formatter til at formatere tidsstempel i HH:mm dd-MM format
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM");

    // Constructor til at oprette en ny ChatMessage med clientId, tidsstempel, type og indhold
    public ChatMessage(String clientId, LocalDateTime timestamp, String type, String content) {
        this.clientId = clientId;
        this.timestamp = timestamp;
        this.type = type;
        this.content = content;
    }

    // Getter til at hente clientId
    public String getClientId() {
        return clientId;
    }

    // Setter til at ændre clientId
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    // Getter til at hente tidsstempel
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Setter til at ændre tidsstempel
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Getter til at hente beskedtypen
    public String getType() {
        return type;
    }

    // Setter til at ændre beskedtypen
    public void setType(String type) {
        this.type = type;
    }

    // Getter til at hente beskedindholdet
    public String getContent() {
        return content;
    }

    // Setter til at ændre beskedindholdet
    public void setContent(String content) {
        this.content = content;
    }

    // Override af toString()-metoden for at formatere ChatMessage som en String
    @Override
    public String toString() {
        // Returnerer en streng der viser clientId, tidsstempel (formatteret), type og indhold
        return clientId + " | " + timestamp.format(formatter) + " | " + type + " | " + content;
    }

    /*
    Metode til at oprette en ChatMessage fra en String
    Modtager hele beskeden som en enkelt String, splitter den op i clientId, timestamp, type og content
    Opretter et nyt ChatMessage objekt baseret på de opdelte data
     */
    public static ChatMessage fromString(String messageStr) {
        // Splitter beskeden op i dele ved hjælp af "|" som separator
        String[] parts = messageStr.split("\\|");
        // Første del er clientId
        String clientId = parts[0];
        // Anden del er tidsstemplet, der konverteres tilbage fra en streng til LocalDateTime
        LocalDateTime timeStamp = LocalDateTime.parse(parts[1], formatter);
        // Tredje del er beskedtypen
        String type = parts[2];
        // Fjerde del er beskedindholdet
        String content = parts[3];
        // Returnerer et nyt ChatMessage objekt
        return new ChatMessage(clientId, timeStamp, type, content);
    }
}
