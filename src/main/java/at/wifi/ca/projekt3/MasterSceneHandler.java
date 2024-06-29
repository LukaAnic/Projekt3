package at.wifi.ca.projekt3;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class MasterSceneHandler {

    public static void changeScene(Node node, String scene, String title) throws IOException {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();

        FXMLLoader loginView = new FXMLLoader(MasterSceneHandler.class.getResource(scene));

        Scene loginScene = new Scene(loginView.load());

        stage.setScene(loginScene);
        stage.setTitle(title);
        stage.show();
    }

    public static void openNewScene(String scene, String title) throws IOException {
        Stage stage = new Stage();

        FXMLLoader addFriend = new FXMLLoader(MasterSceneHandler.class.getResource(scene));
        Scene addFriendScene = new Scene(addFriend.load());

        stage.setScene(addFriendScene);
        stage.setTitle(title);
        stage.show();
    }
}
