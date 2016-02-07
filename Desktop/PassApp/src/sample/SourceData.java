package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Created by Brad on 2/6/2016.
 */
public class SourceData {

    @FXML
    Label sourceTitleFx;

    @FXML
    HBox sourceTileHbox;

    public SourceData() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("source-tile.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setInfo(String string) {
        sourceTitleFx.setText(string);
    }

    public HBox getHbox() {
        return sourceTileHbox;
    }
}
