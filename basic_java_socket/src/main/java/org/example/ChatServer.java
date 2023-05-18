package org.example;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ChatServer {
    static int SIZE = 2;
    private List<ChatHandler> clients;
    private ExecutorService executor;
    public ChatServer() {
        clients = new ArrayList<>();
        executor = Executors.newFixedThreadPool(SIZE);
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat Server started on port " + port);
            while (true) {

                Socket clientSocket = serverSocket.accept();
                if (clients.size()>=SIZE) {
                    System.out.println(clientSocket.getLocalAddress() + " Rejection : Chat Server is crowded ");
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("Sorry, the chat server is currently crowded and unable to accommodate new connections.");
                    clientSocket.close();
                    continue;
                }

                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                ChatHandler chatHandler = new ChatHandler(clientSocket);

                executor.execute(chatHandler);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String message) {
        for (ChatHandler client : clients) {
            try {
                client.writer.write(message + "\n");
                client.writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeClient(ChatHandler client) {
        clients.remove(client);
        System.out.println("Client disconnected: " + client.getClientAddress());
    }

    private class ChatHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader reader;
        private BufferedWriter writer;
        private String clientAddress;

        public ChatHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                clientAddress = clientSocket.getInetAddress().getHostAddress();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getClientAddress() {
            return clientAddress;
        }

        public void run() {
            try {
                clients.add(this);
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("Received from " + clientAddress + ": " + message);
                    broadcast(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                    writer.close();
                    clientSocket.close();
                    removeClient(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start(12345);
    }
}
