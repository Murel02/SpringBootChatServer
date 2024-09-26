package com.example.springbootchatserver.server;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Component
public class ChatServer {
    // Liste til at holde styr på alle tilsluttede klienter (ClientHandler objekter)
    private List<ClientHandler> clients = new ArrayList<>();
    // ExecutorService til at håndtere klientforbindelser i separate tråde
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    /*
    Opretter en server socket, der lytter efter indkommende forbindelser.
    Når en klient tilslutter sig, oprettes et nyt ClientHandler-objekt, som
    lytter efter beskeder fra klienten og sender dem videre til en broadcast-metode,
    der sender beskeden til alle tilsluttede klienter.
    */
    public void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // Lytter kontinuerligt efter nye klientforbindelser
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Accepterer en ny klientforbindelse
                ClientHandler clientHandler = new ClientHandler(clientSocket); // Opretter en ClientHandler til klienten
                clients.add(clientHandler); // Tilføjer klienten til listen over tilsluttede klienter

                // Indstiller en besked-lytter til at håndtere beskeder fra denne klient
                clientHandler.setMessageListener(message -> {
                    System.out.println("Udsender besked fra klient: " + message);
                    broadcastMessage(message);  // Sender beskeden til alle tilsluttede klienter
                });

                // Starter klienthandleren i en ny tråd
                executorService.submit(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Udskriver fejl hvis noget går galt
        }
    }

    // Metode til at sende en besked til alle tilsluttede klienter (broadcast)
    public void broadcastMessage(String message) {
        System.out.println("Udsender besked: " + message);
        for (ClientHandler client : clients) {
            client.sendMessage(message);  // Sender beskeden til hver enkelt klient
        }
    }

    // Metode til at lytte efter beskeder fra alle klienter
    public void onMessageReceived(Consumer<String> listener) {
        System.out.println("Opsætter besked-lytter for alle klienter");
        for (ClientHandler client : clients) {
            client.setMessageListener(listener); // Indstiller besked-lytter for hver klient
        }
    }

    // main-metode til at starte serveren (kan aktiveres når det er nødvendigt)
    public static void main(String[] args) {
        // ChatServer chatServer = new ChatServer();
        // chatServer.startServer(5000);
    }
}
