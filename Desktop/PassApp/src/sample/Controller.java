package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Callback;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class Controller implements Initializable {

    @FXML
    StackPane accountStackPaneFx;

    @FXML
    ListView accountListViewFx;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindComponents();
        addAccountAddButton();
        setAccountListView();
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

    private void setAccountListView() {

        Set<String> stringSet = new HashSet<>();
        ObservableList observableList = FXCollections.observableArrayList();
        stringSet.add("String 1");
        stringSet.add("String 2");
        stringSet.add("String 3");
        observableList.setAll(stringSet);
        accountListViewFx.setItems(observableList);
        accountListViewFx.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView listView) {
                return new ListViewCell();
            }
        });
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


        accountStackPaneFx.getChildren().add(sp);
        StackPane.setAlignment(sp, Pos.BOTTOM_RIGHT);
    }

    public void bindComponents() {
        // TODO handle binds
    }

    public void fillInTestTiles() {

        loadSourceData();
        loadAccountData();
    }

    public void loadSourceData() {

    }

    public void loadAccountData() {

    }
}
