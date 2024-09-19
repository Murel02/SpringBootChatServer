package com.example.springbootchatserver.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private final String hostname;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatClient(String hostname, int port) throws IOException {
        this.hostname = hostname;
        this.port = port;
    }

    public void start(){
        try(Socket socket = new Socket(hostname, port)){
            System.out.println("Client connected to server");

            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread readThread = new Thread(this::readMessages);
            readThread.start();

            Scanner scanner = new Scanner(System.in);

            while (true){
                System.out.println("Enter message: ");
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("exit")){
                    break;
                }

                sendMessage(message);
            }
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            closeConnections();
        }
    }

    public void readMessages(){
        try{
            String message;
            while ((message = in.readLine()) != null){
                System.out.println("Cliient: " + message);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        if (out != null){
            out.println(message);
        }else {
            System.out.println("Error: No connection to server");
        }
    }

    public void closeConnections(){
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
            System.out.println("Client connection closed");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient("localhost", 5000);
        chatClient.start();
    }
}
