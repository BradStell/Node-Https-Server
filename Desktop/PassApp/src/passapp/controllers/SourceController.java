package passapp.controllers;

import javafx.beans.property.ListProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import passapp.Source;

/**
 * Created by Brad on 2/6/2016.
 */
public class SourceController {

    @FXML
    TextField newSourceTfFx;

    @FXML
    Button createSourceFx;

    @FXML
    Button cancelSourceFx;

    @FXML
    Label messageLabelFx;

    ListProperty<Source> sourceListProperty;
    Parent root;
    Stage stage;

    public SourceController(ListProperty<Source> sourceListProperty) {
        this.sourceListProperty = sourceListProperty;

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
        if (newSourceTfFx.getText().equals("")) {
            messageLabelFx.setText("You must enter a name");
        } else {
            sourceListProperty.add(new Source(newSourceTfFx.getText()));
            stage.close();
        }
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
