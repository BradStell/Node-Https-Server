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

    public enum Server {
        ON, OFF
    }

    public static String booty;
    public static Storage storage;
    public static Server server;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/new-main.fxml"));
        primaryStage.setTitle("Pass App");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
