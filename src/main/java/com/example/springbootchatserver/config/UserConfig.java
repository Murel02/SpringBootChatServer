package com.example.springbootchatserver.config;


import com.example.springbootchatserver.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {




    @Bean
    public User user(){
        return new User(1,"Lone","1230");

    }
}
