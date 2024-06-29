package at.wifi.ca.projekt3;

import at.wifi.ca.projekt3.model.User;
import com.password4j.BcryptFunction;
import com.password4j.Password;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    protected TextField emailTextField;
    @FXML
    protected PasswordField passwordTextField;


    @FXML
    protected void onLoginButtonClick(ActionEvent actionEvent) throws SQLException, IOException {

         User user = MasterDAO.UserFindByEmail(emailTextField.getText());

         if (user != null){
             String encryptedPassword = user.getPassword();

             BcryptFunction bcrypt = BcryptFunction.getInstanceFromHash(encryptedPassword);
             boolean verified = Password.check(passwordTextField.getText(), encryptedPassword).with(bcrypt);


             if (verified){
                 DataExchangeHolder.getInstance().setLoggedInUser(user);


                 Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

                 FXMLLoader chatView = new FXMLLoader(getClass().getResource("chat-view.fxml"));
                 Scene chatScene = new Scene(chatView.load());

                 stage.setScene(chatScene);
                 stage.setTitle("Chat");
                 stage.show();
             }
         }
    }

    @FXML
    protected void onRegisterButtonClick(ActionEvent actionEvent) throws IOException {
        // Aktuelles Stage(Bühnen)_Objekt anhand des geklickten Elements auslesen
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.close();

        //Neues Layout laden
        FXMLLoader registrationView = new FXMLLoader(getClass().getResource("registration-view.fxml"));

        //Neue Szene erstellen
        Scene registraionScene = new Scene(registrationView.load());

        //Szene auf bühne setzten
        stage.setScene(registraionScene);
        stage.setTitle("Registrierung");
        stage.show();
    }
}