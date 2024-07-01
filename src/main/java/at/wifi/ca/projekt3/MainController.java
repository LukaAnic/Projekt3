package at.wifi.ca.projekt3;

import at.wifi.ca.projekt3.model.Message;
import at.wifi.ca.projekt3.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    User loggedInUser;

    protected ObservableList<User> onlineFriends = FXCollections.observableArrayList();

    protected ObservableList<Message> messageList = FXCollections.observableArrayList();

    protected ChatClient activeClient = null;

    private Stage mainStage;





    //////////////////////////////////////////////////////////////////// View Ids ////////////////////////////////////////////////////////////////////

    //////// Chat View Ids ////////
    @FXML
    protected CheckMenuItem switchStatusMenuItem;
    @FXML
    protected TextField messageTextField;
    @FXML
    protected ListView<Message> messageListView;
    @FXML
    protected ListView<User> onlineFriendsListView;

    //////// Add Friend View Ids ////////
    @FXML
    protected TextField friendEmailTextField;

    //////// Change User Name Ids ////////
    @FXML
    protected TextField changeUserNameTextField;


    //////////////////////////////////////////////////////////////////// View Methods ////////////////////////////////////////////////////////////////////

    //////// Chat View Methods ////////
    @FXML
    protected void sendMessage(ActionEvent actionEvent) throws IOException, SQLException {
        if (messageTextField.getText() != null){

            Message message = new Message();
            message.setText(messageTextField.getText());
            message.setUserId(loggedInUser.getId());
            int id =  MasterDAO.MessageInsert(message);
            message.setId(id);

            activeClient.sendMessage(message);

            messageTextField.setText("");
        }
    }

    @FXML
    protected void deleteMessageAction(ActionEvent actionEvent) throws IOException {
        Message selectedMessage = messageListView.getSelectionModel().getSelectedItem();

        if (selectedMessage != null){
            activeClient.sendDeleteMessage(selectedMessage);
        }
    }

    @FXML
    protected void logOutMIClick(ActionEvent actionEvent) throws IOException, SQLException {
        loggedInUser.setCurrentlyOnline(false);
        MasterDAO.UserUpdate(loggedInUser);
        loggedInUser = null;
        onlineFriends.clear();
        if (activeClient!= null) {
            activeClient.stopConnection();
        }
        activeClient = null;
        DataExchangeHolder.getInstance().setFriends(onlineFriends);
        DataExchangeHolder.getInstance().setLoggedInUser(null);

        //Es muss messageTextField verwendet werden, da man auf MenuItem nicht Node casten kann
        MasterSceneHandler.changeScene(messageTextField, "login-view.fxml", "Login");
    }

    @FXML
    protected void addFriendMIClick(ActionEvent actionEvent) throws IOException {
        MasterSceneHandler.openNewScene("addFriend-view.fxml", "Add Friend");
    }

    @FXML
    protected void switchStatusClick(ActionEvent actionEvent) throws SQLException {
        loggedInUser.setAlwaysOffline(!loggedInUser.isAlwaysOffline());

        if (MasterDAO.UserUpdate(loggedInUser)){
            switchStatusMenuItem.setSelected(loggedInUser.isAlwaysOffline());
        }else {
            //Zum rückgängig machen
            loggedInUser.setAlwaysOffline(!loggedInUser.isAlwaysOffline());
        }
    }

    @FXML
    protected void changeUserNameMIClick(ActionEvent actionEvent) throws IOException {
        MasterSceneHandler.openNewScene("changeUserName-view.fxml", "Change User Name");
    }

    @FXML
    protected void deleteUserMIClick(ActionEvent actionEvent) throws IOException {
        if (MasterAlerts.confirmation("Delete User", "ATTENTION!!! You are about to delete you data", "If you press OK your Data will be deleted forever")){
            if (MasterAlerts.confirmation("Delete User", "ATTENTION!!! (Again)", "Just in case you don't now that forever is a pretty long time... \n Are you sure?")){
                try {
                    MasterDAO.UserDelete(loggedInUser);
                    Stage stage = (Stage) messageTextField.getScene().getWindow();
                    stage.close();

                    FXMLLoader loginView = new FXMLLoader(getClass().getResource("login-view.fxml"));

                    Scene loginScene = new Scene(loginView.load());

                    stage.setScene(loginScene);
                    stage.setTitle("Login");
                    stage.show();

                } catch (SQLException e) {
                    MasterAlerts.error("Delete User", "Something went wrong", ("Error Message: \n" + e));
                }
            }
        }
    }

    @FXML
    protected void changeEmailMIClick(ActionEvent actionEvent) {
    }

    @FXML
    protected void changePasswordMIClick(ActionEvent actionEvent) {
    }

    @FXML
    protected void removeFriendAction(ActionEvent actionEvent) {
        User selectedFriend = onlineFriendsListView.getSelectionModel().getSelectedItem();

        if (selectedFriend != null){
            try {
                MasterDAO.FriendDelete(selectedFriend);
                onlineFriends.remove(selectedFriend);
            } catch (SQLException e) {
                MasterAlerts.error("Added Friend", "Something went wrong", ("Error Message: \n" + e));
            }
        }
    }

    //////// Add Friend View Methods ////////
    @FXML
    protected void addFriendAction(ActionEvent actionEvent) throws SQLException {
        User friendToAdd = MasterDAO.UserFindByEmail(friendEmailTextField.getText());
        ArrayList<User> existingFriends = (ArrayList<User>) MasterDAO.FriendGetAll(loggedInUser);

        boolean alreadyExistingFriend = false;

        if (friendToAdd!=null){
            for (User existingFriend : existingFriends){

                if (existingFriend.getId() == friendToAdd.getId()){
                    MasterAlerts.warning("Added Friend", "This is already your Friend", "You have entered the Email Address of an existing friend");


                    alreadyExistingFriend = true;
                    break;
                }
            }
        }


        if (friendToAdd != null && !friendToAdd.getEmail().equals(loggedInUser.getEmail()) && !alreadyExistingFriend){
            try {
                MasterDAO.FriendInsert(loggedInUser, friendToAdd);

                MasterAlerts.information("Added Friend", "You have added a new Friend", ("Your are now friends with " + friendToAdd.getUserName()));

                if (!friendToAdd.isAlwaysOffline() && friendToAdd.isCurrentlyOnline()){
                    onlineFriends.add(friendToAdd);
                    DataExchangeHolder.getInstance().setFriends(onlineFriends);
                }

                } catch (SQLException e) {
                MasterAlerts.error("Added Friend", "Something went wrong", ("Error Message: \n" + e));
            }
        } else if (friendToAdd == null){
            MasterAlerts.warning("Added Friend", "Email was not found", "The Email you entered doesn't exist!");
        } else if (friendToAdd.getEmail().equals(loggedInUser.getEmail())){
            MasterAlerts.warning("Added Friend", "This is your Email", "You can't enter your own Email -.-");
        }

        friendEmailTextField.setText("");
    }

    //////// Change User Name Methods ////////
    @FXML
    protected void changeUserNameAction(ActionEvent actionEvent){
        if (!changeUserNameTextField.getText().isEmpty()){

            if (loggedInUser.getUserName().equals(changeUserNameTextField.getText())){
                MasterAlerts.information("\"New\" Username", "This is already your Username -.-", "Your entered the same Username you already have but ok");
            } else {
                loggedInUser.setUserName(changeUserNameTextField.getText());
                try {
                    MasterDAO.UserUpdate(loggedInUser);
                    MasterAlerts.information("New Username", "You changed your Username successfully", ("Your new Username is " + loggedInUser.getUserName()));
                } catch (SQLException e) {
                    MasterAlerts.error("New Username", "Something went wrong", ("Error Message: \n" + e));
                }

            }

            Stage stage = (Stage) changeUserNameTextField.getScene().getWindow();
            stage.close();
        } else {
            MasterAlerts.warning("New Username", "The Text Field is empty", "You need to put your new Username into the Text Field");
        }

    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loggedInUser = DataExchangeHolder.getInstance().getLoggedInUser();
        onlineFriends = DataExchangeHolder.getInstance().getFriends();
        mainStage = DataExchangeHolder.getInstance().getMainStage();


        if (activeClient == null && messageListView != null){


            messageListView.setItems(messageList);

            activeClient = new ChatClient();

            //Listener auf den Client, wenn er eine Nachricht empfängt
            activeClient.getReceivedMessages().addListener((ListChangeListener<? super Message>) c -> {
                while (c.next()) {
                    if (c.wasAdded()) {
                        Platform.runLater(() -> {
                            messageList.addAll(c.getAddedSubList());
                        });
                    }
                }
            });

            activeClient.getDeleteMessages().addListener((ListChangeListener<? super Message>) c ->{
                while (c.next()){
                    if (c.wasAdded()){
                        Platform.runLater(() -> {
                            messageList.removeAll(c.getAddedSubList());
                        });
                    }
                }
            } );

            try {
                activeClient.startConnection();
                loggedInUser.setCurrentlyOnline(true);
                MasterDAO.UserUpdate(loggedInUser);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }


            mainStage.setOnCloseRequest(event -> {
                if (activeClient!= null) {
                    try {
                        activeClient.stopConnection();
                        loggedInUser.setCurrentlyOnline(false);
                        MasterDAO.UserUpdate(loggedInUser);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            messageListView.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {


                public ListCell<Message> call(ListView<Message> userListView) {
                    return new ListCell<Message>() {
                        @Override
                        protected void updateItem(Message message, boolean empty) {
                            super.updateItem(message, empty);
                            if (empty || message == null) {
                                setText(null);
                            } else {
                                try {
                                    String messageContext = message.getText();
                                    String messageSender = MasterDAO.MessageGetUser(message).getUserName();
                                    setText(messageSender + ": " + messageContext);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }
                    };
                }
            });
        }


        if (switchStatusMenuItem != null){
            switchStatusMenuItem.setSelected(loggedInUser.isAlwaysOffline());
        }

        if (this.onlineFriendsListView != null){

            try {
                ArrayList<User> allFriends = (ArrayList<User>) MasterDAO.FriendGetAll(loggedInUser);

                for (User friend : allFriends){
                    if (!friend.isAlwaysOffline() && friend.isCurrentlyOnline()){
                        onlineFriends.add(friend);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            onlineFriendsListView.setItems(onlineFriends);


            onlineFriendsListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {

                //Damit nur der Username in der ListView angezeigt wird
                @Override
                public ListCell<User> call(ListView<User> userListView) {
                    return new ListCell<User>() {
                        @Override
                        protected void updateItem(User user, boolean empty) {
                            super.updateItem(user, empty);
                            if (empty || user == null) {
                                setText(null);
                            } else {
                                setText(user.getUserName());
                            }
                        }
                    };
                }
            });
        }
    }

}
