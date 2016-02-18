package passapp.controllers;

import com.google.gson.JsonObject;
import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import passapp.Account;
import passapp.Main;
import passapp.ServerCommunication.TaskService;
import passapp.Source;
import passapp.CustomOverrides.SourceListViewCell;

import java.util.Set;

/**
 * Created by Brad on 2/6/2016.
 */
public class SourceController {

    @FXML
    TextField newSourceTfFx;

    @FXML
    TextField accountUsernameFx;

    @FXML
    TextField accountPasswordFx;

    @FXML
    Button createSourceFx;

    @FXML
    Button cancelSourceFx;

    @FXML
    Label messageLabelFx;

    ListProperty<Source> sourceListProperty;
    Set<Source> sourceSet;
    ObservableList<Source> observableSourceList;
    ListView sourceListViewFx;

    Parent root;
    Stage stage;

    public SourceController(Set<Source> sourceSet, ObservableList<Source> observableSourceList, ListView sourceListViewFx, ListProperty<Source> sourceListProperty) {
        this.sourceListProperty = sourceListProperty;
        this.sourceSet = sourceSet;
        this.observableSourceList = observableSourceList;
        this.sourceListViewFx = sourceListViewFx;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/add-source.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
            stage = new Stage();
            stage.setTitle("Creat New Source");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onCreateClicked() {
        if (newSourceTfFx.getText().equals("") || accountPasswordFx.getText().equals("") || accountUsernameFx.getText().equals("")) {
            messageLabelFx.setText("You left required fields blank");
        } else {
            Source source = new Source();
            source.setUserSpelledName(newSourceTfFx.getText());
            source.setName(newSourceTfFx.getText().toLowerCase());
            Account account = new Account();
            account.setUsername(accountUsernameFx.getText());
            account.setPassword(accountPasswordFx.getText());
            account.setParent(source);
            source.addAccount(account);
            observableSourceList.add(source);
            stage.close();

            doServerPost();
        }
    }

    private void doServerPost() {

        JsonObject message = new JsonObject();
        message.addProperty("Username", "Brad");
        message.addProperty("Password", "12345");
        message.addProperty("method", "POST");
        JsonObject restOfContent = new JsonObject();
        restOfContent.addProperty("name", newSourceTfFx.getText());
        restOfContent.addProperty("username", accountUsernameFx.getText());
        restOfContent.addProperty("password", accountPasswordFx.getText());
        message.add("restOfContent", restOfContent);

        TaskService service = new TaskService(message.toString());
        service.setOnSucceeded(event -> {
            String messageFromService = (String) event.getSource().getValue();
            System.out.print(messageFromService);
        });
        service.setOnCancelled(event -> {
            System.out.print("Server no online");
        });
        service.start();
    }

    @FXML
    public void onCancelClicked() {
        stage.close();
    }

    @FXML
    public void onTextFieldClicked() {
        messageLabelFx.setText("");
    }
}
