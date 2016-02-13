package passapp.controllers;

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
import passapp.Source;
import passapp.SourceListViewCell;

import java.util.Set;

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
        if (newSourceTfFx.getText().equals("")) {
            messageLabelFx.setText("You must enter a name");
        } else {
            System.out.print(newSourceTfFx.getText() + "---");
            sourceSet.add(new Source(newSourceTfFx.getText()));
            observableSourceList.setAll(sourceSet);
            sourceListViewFx.setItems(observableSourceList);
            sourceListProperty.set(observableSourceList);
            sourceListViewFx.itemsProperty().bind(sourceListProperty);
            sourceListViewFx.setCellFactory(listView -> new SourceListViewCell());
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
