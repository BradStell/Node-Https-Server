package passapp.controllers;

import java.io.*;
import java.net.URL;
import java.util.*;

import brad.crypto.JBSCrypto;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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

    @FXML
    MenuItem menuSyncFx;

    @FXML
    Label informationLabelFx;

    @FXML
    VBox informationBoxFx;

    @FXML
    ToggleGroup storageToggleGroup;

    @FXML
    MenuItem menuKeepStorageFx;

    @FXML
    MenuItem menuNoStorageFx;

    // Hash Sets and properties to bind sets to list view
    Set<Source> sourceSet = new HashSet<>();
    Set<Account> accountSet = new HashSet<>();
    ListProperty<Source> sourceListProperty = new SimpleListProperty<>();
    ListProperty<Account> accountListProperty = new SimpleListProperty<>();
    ObservableList observableSourceList = FXCollections.observableArrayList();
    ObservableList observableAccountList = FXCollections.observableArrayList();

    String lastSync;
    Properties properties;

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
        setSystemSettings();
        sync();

        sourceListViewFx.setOnMouseClicked(sourceListViewClickListener);
        menuSyncFx.setOnAction( event -> sync());
    }

    private void sync() {
        //JSON body message
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Username", "Brad");
        jsonObject.addProperty("Password", "12345");
        jsonObject.addProperty("method", "GET");

        TaskService service = new TaskService(jsonObject.toString());
        service.setOnSucceeded(handleServerDataEvent);
        service.start();
    }

    private void setSystemSettings() {

        Map<String, String> propMap = getPropertiesFromFile();
        String current = "";

        if (propMap != null) {

            if ( (current = propMap.get("Local-Storage")) != null) {
                switch(current) {
                    case "true":
                        storageToggleGroup.selectToggle((Toggle)menuKeepStorageFx);
                        Main.storage = Main.Storage.TRUE;
                        //populateListFromStorage();
                        break;
                    case "false":
                        storageToggleGroup.selectToggle((Toggle)menuNoStorageFx);
                        Main.storage = Main.Storage.FALSE;
                        break;
                    default:
                        storageToggleGroup.selectToggle((Toggle)menuNoStorageFx);
                }
            } else
                storageToggleGroup.selectToggle((Toggle)menuNoStorageFx);

            if ( (current = propMap.get("Color-Theme")) != null) {
                switch (current) {
                    case "default":
                        break;
                    default:
                }
            }
        }
    }

    private Map<String, String> getPropertiesFromFile() {

        File file = new File("src/resources/PassApp.properties");
        properties = new Properties();
        InputStream inputStream;
        Map<String, String> propMap = new HashMap<>();

        try {
            if (! file.exists()) {
                if (! file.createNewFile())
                    System.out.print("File creation failed");
            }

            inputStream = new FileInputStream(file);

            properties.load(inputStream);

            propMap.put("test", properties.getProperty("test"));
            propMap.put("Local-Storage", properties.getProperty("Local-Storage"));
            propMap.put("Color-Theme", properties.getProperty("Color-Theme"));

            return propMap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void populateListFromStorage() {

        File file = new File("src/resources/local-storage.jbs");
        BufferedReader bufferedReader;
        StringBuilder sb = new StringBuilder();
        String line = "";
        JBSCrypto jbsCrypto = new JBSCrypto();

        try {

            if (! file.exists())
                System.out.print("No Storage");
            else {

                bufferedReader = new BufferedReader(new FileReader(file));

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                JsonElement rootElement = new JsonParser().parse(jbsCrypto.decrypt(sb.toString()));
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add source button mouse click listener
     */
    @FXML
    public void sourceAddMouseClicked() {

        SourceController sourceController = new SourceController(sourceSet, observableSourceList, sourceListViewFx, sourceListProperty);
    }

    /**
     * Remove source button mouse click listener
     */
    @FXML
    public void sourceRemoveClicked() {

    }

    /**
     * Edit source button mouse click listener
     */
    @FXML
    public void sourceEditClicked() {

    }

    private void addAccountAddButton() {

        AccountAddButton accountAddButton = new AccountAddButton();
        accountStackPaneFx.getChildren().add(accountAddButton);
        StackPane.setAlignment(accountAddButton, Pos.BOTTOM_RIGHT);
    }

    @FXML
    public void keepStorageMenuListener() {
        Main.storage = Main.Storage.TRUE;
        saveDataToFile(lastSync);
        properties.setProperty("Local-Storage", "true");
        updateProperties();
    }

    @FXML
    public void noStorageMenuListener() {
        Main.storage = Main.Storage.FALSE;
        removeStorageFile();
        properties.setProperty("Local-Storage", "false");
        updateProperties();
    }

    private void updateProperties() {

        File file = new File("src/resources/PassApp.properties");
        OutputStream os;

        try {
            if (! file.exists()) {
                file.createNewFile();
            }

            os = new FileOutputStream(file);
            properties.store(os, "comments");
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        JBSCrypto jbsCrypto = new JBSCrypto();

        ///////////////////////////////////////////
        //      If null then server offline     //
        /////////////////////////////////////////
        if (messageFromService != null) {

            lastSync = jbsCrypto.encrypt(messageFromService);
            Main.server = Main.Server.ON;

            if (Main.storage == Main.Storage.TRUE)
                saveDataToFile(messageFromService);

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
        }


        //////////////////////////////
        //      Server Offline     //
        ////////////////////////////
        else {

            Main.server = Main.Server.OFF;
            if (Main.storage == Main.Storage.TRUE)
                populateListFromStorage();

            informationLabelFx.setText("Error syncing with server. Please make sure server is on. We will operate in offline mode for now.");
            Button okButton = new Button("OK");
            informationBoxFx.getChildren().add(okButton);


            okButton.setOnMouseClicked(event1 -> {
                informationLabelFx.setText("");
                informationBoxFx.getChildren().remove(okButton);
            });
        }
    };

    private void saveDataToFile(String data) {

        File file = new File("src/resources/local-storage.jbs");
        BufferedWriter bufferedWriter;
        JBSCrypto jbsCrypto = new JBSCrypto();

        try {
            if (! file.exists()) {
                if (! file.createNewFile())
                    System.out.print("File creation failed");
            }

            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(jbsCrypto.encrypt(data));
            bufferedWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeStorageFile() {

        File file = new File("src/resources/local-storage.jbs");

        try {
            if (file.exists()) {
                file.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
