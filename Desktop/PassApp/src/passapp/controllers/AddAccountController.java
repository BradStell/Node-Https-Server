package passapp.controllers;

import com.google.gson.JsonObject;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import passapp.Account;
import passapp.DisplayMessageToUser;
import passapp.ServerCommunication.TaskService;
import passapp.Source;

/**
 * Created by Brad on 2/17/2016.
 */
public class AddAccountController {

    @FXML
    TextField usernameFx;

    @FXML
    TextField passwordFx;

    @FXML
    Button okButtonFx;

    @FXML
    Button cancelButtonFx;

    Parent root;
    Stage stage;
    Source parent;
    ObservableList<Account> list;

    public AddAccountController(Source parent, ObservableList<Account> list) {

        this.parent = parent;
        this.list = list;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/add-account-dialog.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
            stage = new Stage();
            stage.setTitle("Create New Account");
            stage.setScene(new Scene(root));
            stage.show();

            okButtonFx.setOnMouseClicked(handlePostEvent);

            cancelButtonFx.setOnMouseClicked(event ->  {
                stage.close();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final EventHandler<MouseEvent> handlePostEvent = event -> {

        System.out.println(usernameFx.getText() + " " + passwordFx.getText());
        String source = parent.getUserSpelledName(), username = usernameFx.getText(), password = passwordFx.getText();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Username", "Brad");
        jsonObject.addProperty("Password", "12345");
        jsonObject.addProperty("method", "POST");
        JsonObject message = new JsonObject();
        message.addProperty("name", source);
        message.addProperty("username", username);
        message.addProperty("password", password);
        jsonObject.add("restOfContent", message);

        TaskService service = new TaskService(jsonObject.toString());
        service.setOnSucceeded(e -> {
            String messageFromService = (String) e.getSource().getValue();
            updateUI(messageFromService);
            stage.close();
        });
        service.setOnCancelled(e -> {
            String messageFromService = (String) e.getSource().getValue();
            updateUI(messageFromService);
            stage.close();
            //TODO handle logic if server is offline
        });
        service.start();
    };

    private void updateUI(String message) {

        System.out.print(message);
        Account account = new Account();
        account.setUsername(usernameFx.getText());
        account.setPassword(passwordFx.getText());

        parent.addAccount(account);
        list.add(account);
        DisplayMessageToUser.displayMessage(message);
    }
}
