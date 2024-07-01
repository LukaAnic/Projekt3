package at.wifi.ca.projekt3;

import at.wifi.ca.projekt3.model.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ChatClient {
    private volatile boolean isConnected;
    private Thread readThread;

    Message receivedMessage;
    ObservableList<Message> receivedMessages = FXCollections.observableArrayList();

    ObservableList<Message> deleteMessages = FXCollections.observableArrayList();

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    Socket socket;

    PrintWriter out;
    BufferedReader in;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;



    public void startConnection() throws IOException{
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        isConnected = true;
        System.out.println("Connected to the chat server!");

         out = new PrintWriter(socket.getOutputStream(), true);
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

         OutputStream outputStream = socket.getOutputStream();
         objectOutputStream = new ObjectOutputStream(outputStream);

         InputStream inputStream = socket.getInputStream();
         objectInputStream = new ObjectInputStream(inputStream);


         //Eingehende Nachrichten empfangen
        readThread = new Thread(() -> {
            try {
                while (isConnected) {
                    Message serverResponse;
                    while ((serverResponse = (Message) objectInputStream.readObject()) != null) {
                        System.out.println("Server Nachricht erhalten: " + serverResponse);
                        receivedMessage = serverResponse;
                        if (serverResponse.isToDelete()) {
                            System.out.println("Nachricht enfentnt");
                            deleteMessages.add(serverResponse);
                        }else {
                            receivedMessages.add(receivedMessage);
                        }

                    }
                }
            } catch (SocketException e) {
                //Fehlermeldung wenn Socket geschlossen wird
                if (!isConnected) {
                    //Schließt den Thread
                    return;
                }
                e.printStackTrace();
            } catch (EOFException e) {
                isConnected = false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        readThread.start();
    }

    public void stopConnection() throws IOException {
        isConnected = false;
        readThread.interrupt();
        objectOutputStream.close();
        objectInputStream.close();
        out.close();
        in.close();
        socket.close();
    }

    public void sendMessage(Message message) throws IOException {
        if (message != null){
            objectOutputStream.writeObject(message);
            System.out.println("Message send: " + message);
        }
    }

    public void sendDeleteMessage(Message deleteMessage) throws IOException{
        if (deleteMessage != null){
            deleteMessage.setToDelete(true);
            this.sendMessage(deleteMessage);
        }
    }


    public ObservableList<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public ObservableList<Message> getDeleteMessages(){
        return deleteMessages;
    }
}
