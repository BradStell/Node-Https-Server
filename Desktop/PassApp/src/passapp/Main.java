package passapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public enum Storage {
        FALSE, TRUE
    }

    public static Storage storage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/new-main.fxml"));
        primaryStage.setTitle("Pass App");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static Storage checkStorageFlag() {
        return storage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
