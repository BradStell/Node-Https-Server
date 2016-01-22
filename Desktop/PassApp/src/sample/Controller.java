package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Controller {

    @FXML
    Pane mainPane;

    @FXML
    AnchorPane mainAnchorPane;

    @FXML
    ScrollPane mainScrollPane;

    public void initialize() throws Exception {

        addTile();
    }

    private void addTile() throws Exception {

        for (int i = 0; i < 20; i++) {
            StackPane tile = FXMLLoader.load(getClass().getResource("tile.fxml"));

            Rectangle rect = new Rectangle(tile.getWidth(), tile.getHeight());
            rect.setFill(null);
            rect.setStroke(Color.BLACK);
            rect.widthProperty().bind(tile.widthProperty());
            rect.heightProperty().bind(tile.heightProperty());

            tile.getChildren().add(rect);

            mainPane.getChildren().add(tile);
        }
    }
}
