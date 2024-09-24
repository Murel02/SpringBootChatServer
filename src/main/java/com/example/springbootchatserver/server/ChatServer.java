package com.example.springbootchatserver.server;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Component
public class ChatServer {
    private List<ClientHandler> clients = new ArrayList<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    /*
    This creates a socket that listens for incoming connections
    When a client connects a new ClientHandler object is created
    It listens for messages from a client and gives it to a broadcast method
    that sends the message to all connected clients
     */
    public void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);

                // Set up a message listener to handle messages from this client
                clientHandler.setMessageListener(message -> {
                    System.out.println("Received message: " + message);
                    broadcastMessage(message);  // Broadcast the message to all clients
                });

                // Start the client handler in a new thread
                executorService.submit(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcast a message to all connected clients
    public void broadcastMessage(String message) {
        System.out.println("Broadcasting message: " + message);
        for (ClientHandler client : clients) {
            client.sendMessage(message);  // Send message to each client
        }
    }

    public void onMessageReceived(Consumer<String> listener){
        for (ClientHandler client : clients){
            client.setMessageListener(listener);
        }
    }

    public static void main(String[] args) {
     //   ChatServer chatServer = new ChatServer();
     //   chatServer. startServer(5000);
    }
}
