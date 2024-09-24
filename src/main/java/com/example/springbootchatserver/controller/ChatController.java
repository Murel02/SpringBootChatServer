package com.example.springbootchatserver.controller;

import com.example.springbootchatserver.model.ChatMessage;
import com.example.springbootchatserver.model.User;
import com.example.springbootchatserver.service.ChatService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;
    private final List<String> chatHistory = new ArrayList<>();
    private final List<SseEmitter> emitters = new ArrayList<>();

    @GetMapping("/")
    public String chatPage(HttpSession session, Model model){
       return "chat";
    }

    @GetMapping("/stream")
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter();
        chatService.listenForIncomingMessages(emitter);  // Listen for new messages and send them via SSE
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }


    /*
    The method runs when you press the send button
    The method uses a message from an HTML file using @RequestParam and a Model object
    If the message is not null or is not an empty message it makes a ChatMessage object using a username from a User object, the current time and a message from the HTML file
    The user is sent to the chat page
     */
    @PostMapping("/send")
    public String sendMessage(@RequestParam("message") String message, Model model) {
        if (message != null && !message.trim().isEmpty()) {
            chatService.sendMessageToServer(message);  // Send message to the chat server
        }
        return "redirect:/";  // Redirect to chat page
    }

}
