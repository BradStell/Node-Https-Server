package sample;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
import java.util.*;

public class Controller implements Initializable {

    @FXML
    StackPane accountStackPaneFx;

    @FXML
    ListView accountListViewFx;

    @FXML
    ListView sourceListViewFx;

    Set<String> sourceSet = new HashSet<>();
    Set<String> accountSet = new HashSet<>();
    ListProperty<String> sourceListProperty = new SimpleListProperty<>();
    ListProperty<String> accountListProperty = new SimpleListProperty<>();
    ObservableList observableSourceList = FXCollections.observableArrayList();
    ObservableList observableAccountList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindComponents();
        addAccountAddButton();

        // Fill in test data
        setSourceListView();
        setAccountListView();

        sourceListProperty.set(observableSourceList);
        accountListProperty.set(observableSourceList);
        sourceListViewFx.itemsProperty().bind(sourceListProperty);
        sourceListViewFx.itemsProperty().bind(accountListProperty);
    }

    @FXML
    public void sourceAddMouseClicked() {

        SourceController sourceController = new SourceController(sourceListProperty);
    }

    @FXML
    public void sourceRemoveClicked() {

        observableSourceList.add("adding");
    }

    @FXML
    public void sourceEditClicked() {

    }

    /**
     * For Testing
     */
    private void setSourceListView() {

        // HashSet is unique, no two same elements.
        // add returns true if successful (element does not already exist)
        // returns false if not successful (element already exists)
        sourceSet.add("Google");
        sourceSet.add("AOC");
        sourceSet.add("Other");
        sourceSet.add("Other2");


        observableSourceList.setAll(sourceSet);
        sourceListViewFx.setItems(observableSourceList);
        sourceListViewFx.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView listView) {
                return new ListViewCell("source");
            }
        });
    }

    /**
     * For Testing
     */
    private void setAccountListView() {

        accountSet.add("String 1~/~Password1");
        accountSet.add("String 2~/~Password2");
        accountSet.add("String 3~/~Password3");
        observableAccountList.setAll(accountSet);
        accountListViewFx.setItems(observableAccountList);
        accountListViewFx.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView listView) {
                return new ListViewCell("account");
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

    public void loadSourceData() {

    }

    public void loadAccountData() {

    }
}
