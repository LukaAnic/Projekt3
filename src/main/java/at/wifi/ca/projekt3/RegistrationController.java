package at.wifi.ca.projekt3;


import at.wifi.ca.projekt3.model.User;
import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Bcrypt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {

    @FXML
    protected TextField userNameField;
    @FXML
    protected TextField emailField;
    @FXML
    protected PasswordField passwordField;


    protected User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.user = new User();

        this.userNameField.textProperty().bindBidirectional(user.userNameProperty());
        this.emailField.textProperty().bindBidirectional(user.emailProperty());
    }

    @FXML
    protected void onSaveBtnClick(ActionEvent actionEvent) {

        BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 12);
        Hash hash = Password.hash(this.passwordField.getText()).with(bcrypt);

        this.user.setPassword(hash.getResult());

        try {
            UserDAO dao = new UserDAO();

           if (dao.create(this.user)){
               Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
               alert.setTitle("Das ist der Titel");
               alert.setHeaderText("Das ist der Kopf");
               alert.setContentText("Das ist der Text");

               Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
               stage.close();

               FXMLLoader loginView = new FXMLLoader(getClass().getResource("login-view.fxml"));

               Scene loginScene = new Scene(loginView.load());

               stage.setScene(loginScene);
               stage.setTitle("Login");
               stage.show();
           }


        } catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler bei der Registrierung");
            alert.setHeaderText("Fehler beim Anlegen");
            alert.setContentText("Beim Anlegen des Users ist ein Fehler aufgetreten: \n" + e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void onBackBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.close();

        FXMLLoader loginView = new FXMLLoader(getClass().getResource("login-view.fxml"));

        Scene loginScene = new Scene(loginView.load());

        stage.setScene(loginScene);
        stage.setTitle("Login");
        stage.show();
    }
}
