package com.example.springbootchatserver.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage {
    private String clientId;
    private LocalDateTime timestamp;
    private String type;
    private String content;

    //format the timestamp
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM");

    public ChatMessage(String clientId, LocalDateTime timestamp, String type, String content) {
        this.clientId = clientId;
        this.timestamp = timestamp;
        this.type = type;
        this.content = content;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return clientId + "|" + timestamp.format(formatter) + "|" + type + "|" + content;
    }

    public static ChatMessage fromString(String messageStr){
        String[] parts = messageStr.split("\\|");
        String clientId = parts[0];
        LocalDateTime timeStamp = LocalDateTime.parse(parts[1], formatter);
        String type = parts[2];
        String content = parts[3];
        return new ChatMessage(clientId, timeStamp, type, content);
    }
}
