package at.wifi.ca.projekt3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {

    private static final int PORT = 12345;
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();



    public static void main(String[] args) {
        try {

            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running and waiting for connections..");

            // Erhält neue Verbindungen
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // Es wird eine Neuer ClientHandler erzeugt für jeden Client, der sich verbindet
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message){
        for (ClientHandler client : clients){
        client.sendMessage(message);
            System.out.println("Message: " + message + " broadcast");
        }

    }


    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        // Constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;

            try {
                // Create input and output streams for communication
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {

                String inputLine;

                // Bekommt Message von Client
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Incomming Message:" + inputLine);

                    // Broadcast the message to all clients
                    broadcast(inputLine);
                }

                // Entfernt Client wenn dieser keine Verbindung mehr hat
                clients.remove(this);
                System.out.println("Client disconnected");

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void sendMessage(String message){
            System.out.println("Sended Message: " + message);
            out.println(message);
        }
    }
}


