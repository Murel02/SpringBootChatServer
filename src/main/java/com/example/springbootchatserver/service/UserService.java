package com.example.springbootchatserver.service;

import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class UserService {

    // Kort (Map) til at gemme brugernavne og adgangskoder i hukommelsen
    private Map<String, String> users = new HashMap<>();
    // Filsti til brugerdatabasefilen
    private final String filePath = "src/users.txt";

    // Konstruktor, der indlæser brugere fra filen ved oprettelse af UserService objektet
    public UserService() {
        loadUsersFromFile();  // Læs eksisterende brugere fra filen
    }

    // Gemmer en bruger til filen ved at tilføje brugernavn og adgangskode
    public void saveUsersToFile(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(username + "=" + password); // Skriver brugernavn og adgangskode i formatet "username=password"
            writer.newLine(); // Tilføjer en ny linje efter hver bruger
        } catch (IOException e) {
            System.err.println("Fejl ved lagring af bruger til fil: " + e.getMessage()); // Udskriver en fejlmeddelelse, hvis der opstår problemer
        }
    }

    // Indlæser brugere fra filen og gemmer dem i users-mappen (Map)
    public void loadUsersFromFile() {
        try {
            Files.lines(Paths.get(filePath)).forEach(line -> {
                String[] parts = line.split("="); // Deler linjen ved lighedstegnet (=)
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]); // Tilføjer brugernavn og adgangskode til users-mappen
                }
            });
        } catch (IOException e) {
            e.printStackTrace(); // Udskriver fejl, hvis der opstår problemer under indlæsning
        }
    }

    // Metode til autentificering af en bruger baseret på brugernavn og adgangskode
    public boolean authenticate(String username, String password) {
        String normalizedUsername = username.toLowerCase(); // Normaliserer brugernavnet til små bogstaver
        return users.containsKey(normalizedUsername) && users.get(normalizedUsername).equals(password); // Tjekker om brugernavn og adgangskode stemmer overens
    }

    // Registrerer en ny bruger, hvis brugernavnet ikke allerede eksisterer
    public boolean registerUser(String username, String password) {
        String normalizedUsername = username.toLowerCase(); // Normaliserer brugernavnet til små bogstaver
        if (users.containsKey(normalizedUsername)) { // Hvis brugernavnet allerede findes, returneres false
            return false;
        } else {
            users.put(normalizedUsername, password); // Tilføjer brugernavnet og adgangskoden til users-mappen
            saveUsersToFile(normalizedUsername, password); // Gemmer brugeren i filen
            return true; // Returnerer true, når brugeren er oprettet
        }
    }
}
