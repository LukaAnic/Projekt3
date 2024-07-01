package at.wifi.ca.projekt3;

import at.wifi.ca.projekt3.model.Message;

import java.io.*;
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

    public static void broadcastSendMessage(Message message) throws IOException {
        for (ClientHandler client : clients){
        client.sendMessage(message);
            System.out.println("Message: " + message + " broadcast");
        }
    }

    public static void broadcastDeleteMessage(Message deleteMessage) throws IOException {
        for (ClientHandler client : clients) {
            client.sendMessage(deleteMessage);
            System.out.println("Delete Message: " + deleteMessage + " broadcast");
        }
    }


    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private ObjectOutputStream objectOutputStream;
        private ObjectInputStream objectInputStream;


        // Constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;

            try {
                // Create input and output streams for communication
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                OutputStream outputStream = clientSocket.getOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);

                InputStream inputStream = clientSocket.getInputStream();
                objectInputStream = new ObjectInputStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                Message inputMessage;

                // Bekommt Message von Client
                while (true) {
                    inputMessage = (Message) objectInputStream.readObject();
                    if (inputMessage == null) {
                        break; // Client hat keine Verbindung
                    }

                    System.out.println("Incoming Message:" + inputMessage);

                    if (inputMessage.isToDelete()){
                        broadcastDeleteMessage(inputMessage);
                    }else {
                        // Übermittelt Nachricht zum senden an Client
                        broadcastSendMessage(inputMessage);
                    }


                }

                // Client hat keine Verbindung
                clients.remove(this);
                System.out.println("Client disconnected");

                closeConnection();

            } catch (EOFException e) {
                // Client hat keine Verbindungd
                clients.remove(this);
                System.out.println("Client disconnected");

                closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


        }

        public void sendMessage(Message message) throws IOException {
            System.out.println("Sended Message: " + message);
            objectOutputStream.writeObject(message);
        }

        private void closeConnection(){
            try {
                in.close();
                out.close();
                objectOutputStream.close();
                objectInputStream.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


