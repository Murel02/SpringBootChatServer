package com.example.springbootchatserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ChatServer server;
    private PrintWriter writer;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    /*
    BufferedReader reads incoming messages from the socket object
    The while loop goes through all incoming messages skipping the empty ones
    And then broadcasts the messages to all connected clients
    The "finally" is used to disconnect a client in case the client disconnects or an error
     */
    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            writer = new PrintWriter(socket.getOutputStream(), true);

            String message;
            while ((message = reader.readLine()) != null) {
                if (message.trim().isEmpty()) {
                    continue;  // Skip empty messages
                }
                System.out.println("Received: " + message);
                server.broadcastMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.removeClient(this);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    Checks if the writer exists
    Sends a message with writer.println(message)
     */
    public void sendMessage(String message) {
        if (writer != null) {
            writer.println(message);
            writer.flush();
        } else {
            System.out.println("Error: No connection to server");
        }
    }
}
