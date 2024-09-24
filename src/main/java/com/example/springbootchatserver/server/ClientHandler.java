package com.example.springbootchatserver.server;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Consumer<String> messageListener; // A listener for incoming messages

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            // Initialize input and output streams for communication
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message;
        try {
            // Continuously listen for messages from the client
            while ((message = in.readLine()) != null) {
                if (messageListener != null) {
                    // Notify the server that a message has been received
                    messageListener.accept(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void setMessageListener(Consumer<String> listener) {
        this.messageListener = listener; // Assign the listener for incoming messages
    }

    // Sends a message to the client
    public void sendMessage(String message) {
        out.println(message);  // Send message to the client
    }

    // Close the connection
    private void closeConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
