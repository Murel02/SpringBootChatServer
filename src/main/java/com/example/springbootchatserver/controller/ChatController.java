package com.example.springbootchatserver.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    private List<String> messages = new ArrayList<>();

    @GetMapping("/chat")
    public String chatPage(Model model){
        model.addAttribute("messages", messages);
        return "chat";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam("message") String message, Model model){
        if (message != null && !message.trim().isEmpty()){
            messages.add(message);
        }
        model.addAttribute("messages", messages);
        return "chat";
    }

}
