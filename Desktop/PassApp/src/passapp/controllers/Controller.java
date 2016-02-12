package passapp.controllers;

import java.net.URL;
import java.util.*;

import brad.crypto.JBSCrypto;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import passapp.*;

/**
 * Main controller for interfacing with new-main.fxml code
 */
public class Controller implements Initializable {

    // UI elements bound by fx:id
    @FXML
    StackPane accountStackPaneFx;

    @FXML
    ListView accountListViewFx;

    @FXML
    ListView sourceListViewFx;

    @FXML
    SplitPane splitPaneFx;

    @FXML
    AnchorPane centerAnchorFx;

    @FXML
    BorderPane rootWindowFx;

    // Hash Sets and properties to bind sets to list view
    Set<Source> sourceSet = new HashSet<>();
    Set<Account> accountSet = new HashSet<>();
    ListProperty<Source> sourceListProperty = new SimpleListProperty<>();
    ListProperty<Account> accountListProperty = new SimpleListProperty<>();
    ObservableList observableSourceList = FXCollections.observableArrayList();
    ObservableList observableAccountList = FXCollections.observableArrayList();

    /**
     * Entry method for controller class
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Create and draw account add button and place on screen
        addAccountAddButton();

        sourceListViewFx.setOnMouseClicked(sourceListViewClickListener);
    }

    /**
     * Add source button mouse click listener
     */
    @FXML
    public void sourceAddMouseClicked() {

        SourceController sourceController = new SourceController(sourceListProperty);
    }

    /**
     * Remove source button mouse click listener
     */
    @FXML
    public void sourceRemoveClicked() {

        // How do add item to source list view with data bind example
        /*observableSourceList.add(new Source("new Source " + ++count));*/
        JBSCrypto jbsCrypto = new JBSCrypto();
        String encrypted = jbsCrypto.encrypt("This is what I want to encrypt");
        System.out.println("\n\nEncrypted ==> " + encrypted);
        String decrypted = jbsCrypto.decrypt(encrypted);
        System.out.println("\n\nDecrypted ==> " + decrypted);
    }

    /**
     * Edit source button mouse click listener
     */
    @FXML
    public void sourceEditClicked() {

        //JSON body message
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Username", "Brad");
        jsonObject.addProperty("Password", "12345");
        jsonObject.addProperty("method", "GET");

        TaskService service = new TaskService(jsonObject.toString());
        //service.setOnSucceeded(handleServerDataEvent);
        service.setOnSucceeded(handleServerDataEvent);
        service.start();
    }

    /**
     * For Testing
     */
    private void setSourceListView() {

    }

    private void addAccountAddButton() {

        AccountAddButton accountAddButton = new AccountAddButton();
        accountStackPaneFx.getChildren().add(accountAddButton);
        StackPane.setAlignment(accountAddButton, Pos.BOTTOM_RIGHT);
    }

    /**
     * Listener for the source list view
     *
     * clears account list view and binds to clicked sources accounts and displays
     */
    private final EventHandler<MouseEvent> sourceListViewClickListener = event -> {

        // clear array lists and list properties and unbind from list view
        if (accountSet.size() > 0) {
            accountSet.clear();
            observableAccountList.clear();
            accountListViewFx.itemsProperty().unbind();
        }

        // Get account list iterator for clicked on Source
        Source source = (Source) sourceListViewFx.getSelectionModel().getSelectedItem();
        Iterator<Account> iterator = source.getAccounts();

        // Add Source's accounts to list view bound set for viewing
        while (iterator.hasNext()) {
            accountSet.add(iterator.next());
        }

        // Bind list view to observable list and bind properties
        observableAccountList.setAll(accountSet);
        accountListViewFx.setItems(observableAccountList);
        accountListProperty.set(observableAccountList);
        accountListViewFx.itemsProperty().bind(accountListProperty);

        // Set cell factory callback to handle custom list cells
        accountListViewFx.setCellFactory(listView -> new AccountListViewCell());
    };

    /**
     *
     */
    private final EventHandler<WorkerStateEvent> handleServerDataEvent = event -> {

        String messageFromService = (String) event.getSource().getValue();
        System.out.println(messageFromService);

        JsonElement rootElement = new JsonParser().parse(messageFromService);
        JsonArray rootArray = rootElement.getAsJsonArray();

        for (int i = 0; i < rootArray.size(); i++) {
            Source source = new Source();

            JsonObject sourceObject = rootArray.get(i).getAsJsonObject();
            JsonArray accountArray = sourceObject.getAsJsonArray("accounts");

            source.setId(sourceObject.get("_id").getAsString());
            source.setName(sourceObject.get("name").getAsString());
            source.setUserSpelledName(sourceObject.get("userSpelledName").getAsString());

            for (int j = 0; j < accountArray.size(); j++) {
                JsonObject accountObj = accountArray.get(j).getAsJsonObject();
                Account account = new Account();
                account.setUsername(accountObj.get("username").getAsString());
                account.setPassword(accountObj.get("password").getAsString());

                source.addAccount(account);
            }

            sourceSet.add(source);
        }

        observableSourceList.setAll(sourceSet);
        sourceListViewFx.setItems(observableSourceList);
        sourceListProperty.set(observableSourceList);
        sourceListViewFx.itemsProperty().bind(sourceListProperty);
        sourceListViewFx.setCellFactory(listView -> new SourceListViewCell());
    };
}
