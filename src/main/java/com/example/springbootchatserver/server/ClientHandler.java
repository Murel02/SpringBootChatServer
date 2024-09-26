package com.example.springbootchatserver.server;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientHandler implements Runnable {
    // Socket til at kommunikere med klienten
    private final Socket clientSocket;
    // Reader til at modtage data fra klienten
    private BufferedReader in;
    // Writer til at sende data til klienten
    private PrintWriter out;
    // En listener, der lytter til modtagne beskeder
    private Consumer<String> messageListener;

    // Constructor, der modtager en socket og sætter input/output streams op
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            // Initialiser input stream til at modtage beskeder fra klienten
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // Initialiser output stream til at sende beskeder til klienten
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Kører som en separat tråd, der håndterer klientens kommunikation
    @Override
    public void run() {
        String message;
        try {
            // Lytter konstant efter beskeder fra klienten
            while ((message = in.readLine()) != null) {
                System.out.println("Besked modtaget fra klient: " + message);
                // Hvis der er en listener, sendes beskeden videre til serveren
                if (messageListener != null) {
                    messageListener.accept(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Lukker forbindelsen, når klienten er færdig
            closeConnection();
        }
    }

    // Setter for at angive en besked-lytter
    public void setMessageListener(Consumer<String> listener) {
        this.messageListener = listener;  // Tildeler lytteren til modtagne beskeder
    }

    // Metode til at sende en besked til klienten
    public void sendMessage(String message) {
        try {
            // Sender beskeden til klienten
            out.println(message);
        }catch (Exception e){
            System.err.println("Kunne ikke sende besked: " + e.getMessage());
        }
    }

    // Lukker forbindelsen til klienten
    private void closeConnection() {
        try {
            in.close();   // Lukker input stream
            out.close();  // Lukker output stream
            clientSocket.close();  // Lukker socketforbindelsen
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
