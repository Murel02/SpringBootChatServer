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

import java.time.LocalDateTime;

@Controller
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/chat")
    public String chatPage(HttpSession session, Model model){
        String username = (String) session.getAttribute("username");
        if (username != null){
            model.addAttribute("username", username);
            return "redirect:/chat.html";
        } else {
            return "redirect:/login";
        }
    }

    /*
    The method runs when you press the send button
    The method uses a message from an HTML file using @RequestParam and a Model object
    If the message is not null or is not an empty message it makes a ChatMessage object using a username from a User object, the current time and a message from the HTML file
    The user is sent to the chat page
     */
    @PostMapping("/send")
    public String sendMessage(@RequestParam("message") String message, User user, Model model){
        if (message != null && !message.trim().isEmpty()){
            ChatMessage chatMessage = new ChatMessage(user.getUsername(), LocalDateTime.now(), "TEXT", message);
            chatService.handleMessage(chatMessage.toString());
        }
        return "redirect:/chat";
    }
}
