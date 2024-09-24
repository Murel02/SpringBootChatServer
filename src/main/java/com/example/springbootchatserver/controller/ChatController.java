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

    @GetMapping("/chat")
    public String chatPage(HttpSession session, Model model){
        //check if the user is loggged in
        String username = (String) session.getAttribute("username");

        if (username == null){
            return "redirect:/login";
        }

        List<ChatMessage> messages = chatService.getChatMessages();
        model.addAttribute("messages", messages);
       return "chat";
    }


    @GetMapping("/stream")
    public SseEmitter stream(HttpSession session) {
        //Check if the user is logged in
        String username = (String) session.getAttribute("username");
        if (username == null){
            throw new IllegalStateException("User must be logged in to acces this endpoint.");
        }
       SseEmitter emitter = new SseEmitter(-1L);
       chatService.listenForIncomingMessages(emitter);
        return emitter;
    }


    /*
    The method runs when you press the send button
    The method uses a message from an HTML file using @RequestParam and a Model object
    If the message is not null or is not an empty message it makes a ChatMessage object using a username from a User object, the current time and a message from the HTML file
    The user is sent to the chat page
     */
    @PostMapping("/send")
    public String sendMessage(@RequestParam("message") String message, HttpSession session) {
        //Retrieve username from the session
        String username = (String) session.getAttribute("username");

        if (username == null){
            return "redirect:/login";
        }

        LocalDateTime timestamp = LocalDateTime.now();
        String type = "TEXT";

        ChatMessage newMessage = new ChatMessage(username,timestamp,type,message);

        //add the message to the session to track the last sent message
        session.setAttribute("lastSentMessage", newMessage);

        chatService.broadcastMessage(newMessage);
        return "redirect:/chat";  // Redirect to chat page
    }

}
