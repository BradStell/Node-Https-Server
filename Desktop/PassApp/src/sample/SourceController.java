package sample;

import javafx.beans.property.ListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by Brad on 2/6/2016.
 */
public class SourceController {

    @FXML
    TextField newSourceTfFx;

    @FXML
    Button createSourceFx;

    List<String> sourceList;
    ListProperty<String> listProperty;
    ListView lv;

    public SourceController(List<String> list, ListProperty<String> lp, ListView lv) {
        sourceList = list;
        listProperty = lp;
        this.lv = lv;

        Parent root;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-source.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Creat New Source");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createClicked() {
        sourceList.add(newSourceTfFx.getText());
        lv.getItems().clear();
    }
}
