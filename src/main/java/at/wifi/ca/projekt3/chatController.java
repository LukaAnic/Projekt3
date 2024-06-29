package at.wifi.ca.projekt3;

import at.wifi.ca.projekt3.model.User;
import javafx.collections.FXCollections;
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

public class chatController implements Initializable {


    User loggedInUser;

    protected ObservableList<User> friends;



    //////////////////////////////////////////////////////////////////// View Ids ////////////////////////////////////////////////////////////////////

    //////// Chat View Ids ////////
    @FXML
    protected CheckMenuItem switchStatusMenuItem;
    @FXML
    protected TextField messageTextField;
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
    protected void sendMessage(ActionEvent actionEvent) {

    }

    @FXML
    protected void logOutMIClick(ActionEvent actionEvent) throws IOException {
        loggedInUser = null;

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

        if (switchStatusMenuItem != null){
            switchStatusMenuItem.setSelected(loggedInUser.isAlwaysOffline());
        }

        if (this.onlineFriendsListView != null){
            friends = FXCollections.observableArrayList();
            try {
                friends.addAll(MasterDAO.FriendGetAll(loggedInUser));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            onlineFriendsListView.setItems(friends);

            //Damit nur der Username in der ListView angezeigt wird
            onlineFriendsListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
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



    //Controller Methods


    public void setLoggedInUser(User logedInUser) {
        this.loggedInUser = logedInUser;
    }

}
