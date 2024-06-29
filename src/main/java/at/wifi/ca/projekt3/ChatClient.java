package at.wifi.ca.projekt3;

import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    String receivedMessage;

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    PrintWriter out;
    BufferedReader in;


    public void startConnection(ObservableList<String> messageList) throws IOException{
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        System.out.println("Connected to the chat server!");

         out = new PrintWriter(socket.getOutputStream(), true);
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

         //Eingehende Nachrichten empfangen
        new Thread(() -> {
            try {
                String serverResponse;
                while ((serverResponse = in.readLine()) != null) {
                    System.out.println("Server Nachricht erhalten: " + serverResponse);
                    receivedMessage = serverResponse;
                    messageList.add(receivedMessage);
                    System.out.println("All send Messages: " + messageList);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendMessage(String message){
        if (message != null){
            out.println(message);
            System.out.println("Message send: " + message);
        }
    }


}
