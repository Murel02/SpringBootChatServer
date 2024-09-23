package com.example.springbootchatserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatServer {
    private final int port; // Port, som serveren lytter på
    private final Set<ClientHandler> clientHandlers; // Sæt til at holde styr på alle klienter
    private final ExecutorService pool; // Tråd-pool til at håndtere klientforbindelser
    private ServerSocket serverSocket; // ServerSocket til at acceptere forbindelser fra klienter

    // Konstruktor til at oprette serveren
    public ChatServer(int port) {
        this.port = port; // Initialiserer portnummeret
        int threadPoolSize = 10; // Størrelse af tråd-pool
        pool = Executors.newFixedThreadPool(threadPoolSize); // Opretter en tråd-pool med et fast antal tråde
        clientHandlers = Collections.synchronizedSet(new HashSet<>()); // Synkroniseret sæt for trådsikkerhed

        // Tilføjer en shutdown-hook for at lukke serveren ned korrekt, når applikationen stopper
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    // Metode til at starte serveren
    public void start() {
        try {
            serverSocket = new ServerSocket(port); // Opretter en server socket, der lytter på den angivne port
            System.out.println("Server lytter på port " + port);

            // Løkke til at acceptere nye klientforbindelser
            while (!serverSocket.isClosed()) {
                try {
                    Socket socket = serverSocket.accept(); // Accepterer en ny klientforbindelse
                    System.out.println("Ny klient forbundet.");

                    // Opretter en ny ClientHandler til at håndtere klienten
                    ClientHandler clientHandler = new ClientHandler(socket, this);
                    clientHandlers.add(clientHandler); // Tilføjer klienten til listen af klienthåndterere
                    pool.execute(clientHandler); // Kører ClientHandler i tråd-poolen
                } catch (IOException e) {
                    // Hvis serveren er lukket, stop med at acceptere klienter
                    if (serverSocket.isClosed()) {
                        System.out.println("Server socket er lukket, stopper med at acceptere klienter.");
                        break;
                    }
                    // Håndterer fejl ved accept af klientforbindelser
                    System.err.println("Fejl ved accept af klientforbindelse: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            // Håndterer fejl, hvis der er problemer med serverens I/O
            System.err.println("Server I/O fejl: " + e.getMessage());
        }
    }

    // Metode til at fjerne en klient fra listen, når de afbryder forbindelsen
    public void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler); // Fjerner klienten fra listen af aktive klienter
        System.out.println("En klient er blevet afbrudt");
    }

    // Metode til at sende en besked til alle tilknyttede klienter
    public synchronized void broadcastMessage(String message) {
        if (message != null && !message.trim().isEmpty()) { // Tjekker om beskeden ikke er tom
            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.sendMessage(message); // Sender beskeden til hver klient
            }
        }
    }

    // Metode til at stoppe serveren
    public void stop() {
        try {
            System.out.println("Lukker serveren...");

            // Lukker server socket for at stoppe med at acceptere nye klienter
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

            // Lukker tråd-poolen ned på en kontrolleret måde
            pool.shutdown();
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Tvinger tråd-poolen til at lukke ned...");
                pool.shutdownNow(); // Tvinger lukning af tråd-poolen, hvis den ikke lukker inden for tidsgrænsen
            }
        } catch (IOException | InterruptedException e) {
            // Håndterer eventuelle fejl ved nedlukning af serveren
            System.err.println("Fejl ved servernedlukning: " + e.getMessage());
        }
    }

    // Main-metode til at starte serveren
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(5000); // Opretter serveren på port 5000
        chatServer.start(); // Starter serveren
    }
}
