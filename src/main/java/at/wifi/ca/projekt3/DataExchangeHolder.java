package at.wifi.ca.projekt3;

import at.wifi.ca.projekt3.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class DataExchangeHolder {
    private User loggedInUser;

    private ObservableList<User> friends = FXCollections.observableArrayList();

    private String receivedMessage;

    private static DataExchangeHolder INSTANCE;

    private DataExchangeHolder(){}

    public static DataExchangeHolder getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DataExchangeHolder();
        }
        return INSTANCE;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public ObservableList<User> getFriends() {
        return friends;
    }

    public void setFriends(ObservableList<User> friends) {
        this.friends = friends;
    }

    public String getReceivedMessage() {
        return receivedMessage;
    }

    public void setReceivedMessage(String receivedMessage) {
        this.receivedMessage = receivedMessage;
    }
}
