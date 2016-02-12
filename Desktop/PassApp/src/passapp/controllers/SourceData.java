package passapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import passapp.Source;

/**
 * Created by Brad on 2/6/2016.
 */
public class SourceData {

    @FXML
    Label sourceTitleFx;

    @FXML
    HBox sourceTileHboxFx;

    public SourceData() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/source-tile.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.print("\n\nIn source fxml load catch\n\n");
        }
    }

    public void setInfo(Source source) {
        if (sourceTitleFx == null) {
            System.out.print("\n\nIts null\n\n");
        } else {
            sourceTitleFx.setText(source.getUserSpelledName());
        }
    }

    public HBox getHbox() {
        return sourceTileHboxFx;
    }
}
