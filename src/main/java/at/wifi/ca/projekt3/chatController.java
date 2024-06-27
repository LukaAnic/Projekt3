package at.wifi.ca.projekt3;

import at.wifi.ca.projekt3.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class chatController implements Initializable {
    User loggedInUser;

    //View Methods
    @FXML
    public void sendMessage(ActionEvent actionEvent) {
    }

    @FXML
    public void logOutClick(ActionEvent actionEvent) {
    }

    @FXML
    public void addFriendClick(ActionEvent actionEvent) {
    }

    @FXML
    public void switchStatusClick(ActionEvent actionEvent) {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }



    //Controller Methods
    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User logedInUser) {
        this.loggedInUser = logedInUser;
        System.out.println(logedInUser);
    }
}
