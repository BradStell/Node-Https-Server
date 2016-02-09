package passapp.controllers;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Callback;
import passapp.*;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    StackPane accountStackPaneFx;

    @FXML
    ListView accountListViewFx;

    @FXML
    ListView sourceListViewFx;
    static int count = 0;

    // Hash Sets and properties to bind sets to list view
    Set<Source> sourceSet = new HashSet<>();
    Set<Account> accountSet = new HashSet<>();
    ListProperty<Source> sourceListProperty = new SimpleListProperty<>();
    ListProperty<Account> accountListProperty = new SimpleListProperty<>();
    ObservableList observableSourceList = FXCollections.observableArrayList();
    ObservableList observableAccountList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindComponents();
        addAccountAddButton();

        // Fill in test data
        setSourceListView();
        //setAccountListView();

        sourceListViewFx.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                accountListViewFx.getItems().clear();
                accountSet.clear();
                observableAccountList.clear();

                Source source = (Source) sourceListViewFx.getSelectionModel().getSelectedItem();
                Iterator<Account> iterator = source.getAccounts();
                while (iterator.hasNext()) {
                    accountSet.add(iterator.next());
                }
                observableAccountList.setAll(accountSet);
                accountListViewFx.setItems(observableAccountList);
                accountListProperty.set(observableAccountList);
                accountListViewFx.itemsProperty().bind(accountListProperty);
                accountListViewFx.setCellFactory(new Callback<ListView, ListCell>() {
                    @Override
                    public ListCell call(ListView listView) {
                        return new AccountListViewCell();
                    }
                });
            }
        });
    }

    @FXML
    public void sourceAddMouseClicked() {

        SourceController sourceController = new SourceController(sourceListProperty);
    }

    @FXML
    public void sourceRemoveClicked() {

        observableSourceList.add(new Source("new Source " + ++count));
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

        Source[] sources = new Source[4];
        for (int i = 0; i < 4; i++)
            sources[i] = new Source("Source " + ++count);

        sources[0].addAccount(new Account("Username Uno", "Password Uno"));
        sources[0].addAccount(new Account("Username UnoUno", "Password UnoUno"));
        sources[1].addAccount(new Account("Username Dos", "Password Dos"));
        sources[2].addAccount(new Account("Username Tres", "Password Tres"));
        sources[3].addAccount(new Account("Username Quatro", "Password Quatro"));

        for (int i = 0; i < 4; i++)
            sourceSet.add(sources[i]);

        observableSourceList.setAll(sourceSet);
        sourceListViewFx.setItems(observableSourceList);
        sourceListProperty.set(observableSourceList);
        sourceListViewFx.itemsProperty().bind(sourceListProperty);
        sourceListViewFx.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView listView) {
                return new SourceListViewCell();
            }
        });
    }

    /**
     * For Testing
     */
    private void setAccountListView() {

        /*accountSet.add(new Account("String 1", "Password1"));
        accountSet.add(new Account("String 2", "Password2"));
        accountSet.add(new Account("String 3", "Password3"));
        observableAccountList.setAll(accountSet);
        accountListViewFx.setItems(observableAccountList);
        accountListProperty.set(observableAccountList);
        accountListViewFx.itemsProperty().bind(accountListProperty);
        accountListViewFx.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView listView) {
                return new AccountListViewCell();
            }
        });*/
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
        circle.setStyle("-fx-cursor: hand");


        Line vert = new Line();
        vert.setStartX(17.0f);
        vert.setStartY(3.0f);
        vert.setEndX(17.0f);
        vert.setEndY(37.0f);
        vert.setStroke(Color.WHITE);
        vert.setStrokeWidth(10.0f);
        vert.setId("#vertRect");
        vert.setStyle("-fx-cursor: hand");

        Line horz = new Line();
        horz.setStartX(3.0f);
        horz.setStartY(17.0f);
        horz.setEndX(37.0f);
        horz.setEndY(17.0f);
        horz.setStroke(Color.WHITE);
        horz.setStrokeWidth(10.0f);
        horz.setStyle("-fx-cursor: hand");

        setMouseEvents(circle, vert, horz);

        sp.getChildren().add(circle);
        sp.getChildren().add(vert);
        sp.getChildren().add(horz);


        accountStackPaneFx.getChildren().add(sp);
        StackPane.setAlignment(sp, Pos.BOTTOM_RIGHT);
    }

    private void setMouseEvents(Circle circle, Line vert, Line horz) {
        circle.setOnMouseEntered( event -> {
            circle.setFill(Color.CYAN.darker());
            vert.setStrokeWidth(13.0f);
            horz.setStrokeWidth(13.0f);
        });

        circle.setOnMouseExited( event -> {
            circle.setFill(Color.CYAN);
            vert.setStrokeWidth(10.0f);
            horz.setStrokeWidth(10.0f);
        });

        horz.setOnMouseEntered( event -> {
            circle.setFill(Color.CYAN.darker());
            vert.setStrokeWidth(13.0f);
            horz.setStrokeWidth(13.0f);
        });

        horz.setOnMouseExited( event -> {
            circle.setFill(Color.CYAN);
            vert.setStrokeWidth(10.0f);
            horz.setStrokeWidth(10.0f);
        });

        vert.setOnMouseEntered( event -> {
            circle.setFill(Color.CYAN.darker());
            vert.setStrokeWidth(13.0f);
            horz.setStrokeWidth(13.0f);
        });

        vert.setOnMouseExited( event -> {
            circle.setFill(Color.CYAN);
            vert.setStrokeWidth(10.0f);
            horz.setStrokeWidth(10.0f);
        });
    }

    public void bindComponents() {
        // TODO handle binds
    }

    public void loadSourceData() {

    }

    public void loadAccountData() {

    }
}
