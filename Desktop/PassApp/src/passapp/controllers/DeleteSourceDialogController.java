package passapp.controllers;

import com.google.gson.JsonObject;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import passapp.DisplayMessageToUser;
import passapp.Main;
import passapp.ServerCommunication.TaskService;
import passapp.Source;

/**
 * Created by Brad on 2/18/2016.
 */
public class DeleteSourceDialogController {

    Parent root;
    Stage stage;
    Source source;
    ObservableList<Source> list;
    ListView listView;

    public DeleteSourceDialogController(Source source, ObservableList<Source> list, ListView listView) {

        this.list = list;
        this.listView = listView;
        this.source = source;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/delete-source-dialog.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
            stage = new Stage();
            stage.setTitle("Warning!");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void okButtonClicked() {
        stage.close();

        list.remove(source);
        listView.getItems().clear();

        // TODO handle delete
        JsonObject message = new JsonObject();
        message.addProperty("Username", "Brad");
        message.addProperty("Password", "12345");
        message.addProperty("method", "DELETE");
        JsonObject restOfContent = new JsonObject();
        restOfContent.addProperty("name", source.getUserSpelledName());
        restOfContent.addProperty("whatToDelete", "document");
        restOfContent.addProperty("username", "ignored");
        message.add("restOfContent", restOfContent);

        TaskService service = new TaskService(message.toString());
        service.setOnSucceeded(succeedEvent);
        service.setOnCancelled(cancelEvent);
        service.start();
    }

    @FXML
    public void cancelButtonClicked() {
        stage.close();
    }

    private final EventHandler<WorkerStateEvent> succeedEvent = event -> {
        String messageFromServer = (String) event.getSource().getValue();
        System.out.print(messageFromServer);
    };

    private final EventHandler<WorkerStateEvent> cancelEvent = event -> {
        String messageFromServer = (String) event.getSource().getValue();
        System.out.print(messageFromServer);
    };
}
