package com.example.springbootchatserver.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

@Component
public class ChatClient {
    private final String hostname;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatClient(@Value("${chat.client.hostname}") String hostname,
                      @Value("${chat.client.port}") int port) {
        this.hostname = hostname;
        this.port = port;
    }

   public void connect() throws IOException{
        if (socket == null || socket.isClosed()){
            socket = new Socket(hostname, port);
            out = new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
   }

    public void sendMessage(String message) throws IOException{
        if (out != null){
            out.println(message);
            out.flush();
        }else {
            throw new IOException("Not connected to sever");
        }
    }

    public String receiveMessage() throws IOException{
        if (in != null){
            return in.readLine();
        }else {
            throw new IOException("Not connected to sever");
        }
    }

    public void close() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
            System.out.println("Client connection closed");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
