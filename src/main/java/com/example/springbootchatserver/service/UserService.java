package com.example.springbootchatserver.service;



import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private Map<String, String> users = new HashMap<>();
    private final String filePath = "src/users.txt";



    public UserService(){
        loadUsersFromFile();
    }

    private void saveUsersToFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))){
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }

        } catch (IOException e){
            System.err.println("Error saving user to file : " + e.getMessage());
        }


    }

    private void loadUsersFromFile(){
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

    public boolean authenticate(String username, String password){
        if (users.containsKey(username)){
            return users.get(username).equals(password);
        }
        return false;
    }

    public boolean registerUser(String username, String password){
        if (users.containsKey(username)){
            return false;
        } else {
            users.put(username, password);
            saveUsersToFile();
            return true;
        }
    }
}
