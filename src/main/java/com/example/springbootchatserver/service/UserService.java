package com.example.springbootchatserver.service;



import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class UserService{

    private Map<String, String> users = new HashMap<>();
    private final String filePath = "src/users.txt";


    public UserService(){
        loadUsersFromFile();
    }

    public void saveUsersToFile(String username, String password){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))){
            writer.write(username + "=" + password);
            writer.newLine();
        } catch (IOException e){
            System.err.println("Error saving user to file : " + e.getMessage());
        }
    }

    public void loadUsersFromFile(){
       try{
           Files.lines(Paths.get(filePath)).forEach(line -> {
               String[] parts = line.split(("="));
               if (parts.length == 2){
                    users.put(parts[0], parts[1]);
               }
           });
       }catch (IOException e){
           e.printStackTrace();
       }
    }

    public boolean authenticate(String username, String password) {
        String normalizedUsername = username.toLowerCase();
        return users.containsKey(normalizedUsername) && users.get(normalizedUsername).equals(password);
    }

    public boolean registerUser(String username, String password){
       String normalizedUsername = username.toLowerCase();
       if (users.containsKey(normalizedUsername)){
           return false;
       } else {
           users.put(normalizedUsername, password);
           saveUsersToFile(normalizedUsername, password);
           return true;
       }
    }
}
