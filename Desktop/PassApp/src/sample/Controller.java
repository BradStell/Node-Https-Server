package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    ScrollPane sourceScrollPane;

    @FXML
    VBox sourceScrollBox;

    @FXML
    ScrollPane accountScrollPane;

    @FXML
    StackPane accountStackPane;

    @FXML
    VBox accountScrollBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindComponents();
        addAccountAddButton();
        fillInTestTiles();
    }

    @FXML
    public void sourceAddMouseClicked() {

    }

    @FXML
    public void sourceRemoveClicked() {

    }

    @FXML
    public void sourceEditClicked() {

    }

    private void addAccountAddButton() {

        StackPane sp = new StackPane();
        sp.setMaxWidth(40.0f);
        sp.setMaxHeight(40.0f);
        sp.setPadding(new Insets(0.0f, 10.0f, 20.0f, 0.0f));

        Circle circle = new Circle();
        circle.setRadius(30.0f);
        circle.setFill(Color.CYAN);
        circle.setEffect(new DropShadow(40.0f, 10.0f, 10.0f, Color.BLACK));

        Line vert = new Line();
        vert.setStartX(17.0f);
        vert.setStartY(3.0f);
        vert.setEndX(17.0f);
        vert.setEndY(37.0f);
        vert.setStroke(Color.WHITE);
        vert.setStrokeWidth(10.0f);

        Line horz = new Line();
        horz.setStartX(3.0f);
        horz.setStartY(17.0f);
        horz.setEndX(37.0f);
        horz.setEndY(17.0f);
        horz.setStroke(Color.WHITE);
        horz.setStrokeWidth(10.0f);

        sp.getChildren().add(circle);
        sp.getChildren().add(vert);
        sp.getChildren().add(horz);


        accountStackPane.getChildren().add(sp);
        StackPane.setAlignment(sp, Pos.BOTTOM_RIGHT);
    }

    public void bindComponents() {
        sourceScrollBox.prefWidthProperty().bind(sourceScrollPane.widthProperty());
        sourceScrollBox.prefHeightProperty().bind(sourceScrollPane.heightProperty());

        accountStackPane.prefWidthProperty().bind(accountScrollPane.widthProperty());
        accountStackPane.prefHeightProperty().bind(accountScrollPane.heightProperty());
    }

    public void fillInTestTiles() {

        loadSourceData();
        loadAccountData();
    }

    public void loadSourceData() {

        for (int i = 0; i < 20; i++) {
            HBox tile;

            try {
                tile = FXMLLoader.load(getClass().getResource("source-tile.fxml"));
                sourceScrollBox.getChildren().add(tile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sourceScrollPane.setPannable(true);
        sourceScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sourceScrollPane.setFitToWidth(true);

    }

    public void loadAccountData() {

        for (int i = 0; i < 20; i++) {
            VBox tile;

            try {
                tile = FXMLLoader.load(getClass().getResource("account-tile.fxml"));
                accountScrollBox.getChildren().add(tile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        accountScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        accountScrollPane.setFitToWidth(true);
    }
}
