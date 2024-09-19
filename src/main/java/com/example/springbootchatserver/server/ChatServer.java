package com.example.springbootchatserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatServer {
    private final int port;
    private final int threadPoolSize = 10;
    private Set<ClientHandler> clientHandlers = new HashSet<>();
    private ExecutorService pool;

    public ChatServer(int port) {
        this.port = port;
        pool = Executors.newFixedThreadPool(threadPoolSize);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Shutting down server...");
                pool.shutdown();
                pool.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                System.err.println("Server shutdown interrupted: " + e.getMessage());
            }
        }));
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected.");

                ClientHandler clientHandler = new ClientHandler(socket, this);
                clientHandlers.add(clientHandler);
                pool.execute(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Server I/O error: " + e.getMessage());
        }
    }

    public void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        System.out.println("A client has disconnected");
    }

    public synchronized void broadcastMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.sendMessage(message);
            }

        }
    }
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(5000);
        chatServer.start();
    }
}
