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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import passapp.*;
import passapp.CustomOverrides.AccountAddButton;
import passapp.CustomOverrides.AccountListViewCell;
import passapp.CustomOverrides.SourceListViewCell;
import passapp.ServerCommunication.TaskService;

/**
 * Main controller for interfacing with new-main.fxml code
 */
public class Controller implements Initializable {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////                                                          //////////////////////////
    //                              FXML UI LINKED COMPONENTS                                          //
    /////////////////                                                          /////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    @FXML
    BorderPane rootWindowFx;            // Root UI element

    ////////////////
    ///  TOP    ///
    @FXML
    MenuItem menuSyncFx;                // serverGET menu item

    // Menu
    @FXML
    ToggleGroup storageToggleGroup;     // local storage menu item toggle group

    @FXML
    MenuItem menuKeepStorageFx;         // keep local storage menu item

    @FXML
    MenuItem menuNoStorageFx;           // do not keep local storage menu item

    ////////////////
    ///  LEFT   ///
    @FXML
    VBox informationBoxFx;              // information display area

    /////////////////
    ///  CENTER  ///
    @FXML
    StackPane accountStackPaneFx;       // account (right) split pane stack pane (all items added to this)

    @FXML
    ListView accountListViewFx;         // account (right) side list view

    @FXML
    ListView sourceListViewFx;          // source (left) side list view

    @FXML
    SplitPane splitPaneFx;              // center split pane

    @FXML
    AnchorPane centerAnchorFx;          // center root anchor pane

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////                                                          //////////////////////////
    //                              GLOBAL OBJECTS                                                     //
    /////////////////                                                          /////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    // Hash Sets and properties to bind sets to list view
    Set<Source> sourceSet = new HashSet<>();
    Set<Account> accountSet = new HashSet<>();
    ListProperty<Source> sourceListProperty = new SimpleListProperty<>();
    ListProperty<Account> accountListProperty = new SimpleListProperty<>();
    ObservableList observableSourceList = FXCollections.observableArrayList();
    ObservableList observableAccountList = FXCollections.observableArrayList();

    // Properties file filled from PassApp.properties file
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
        fullServerSync();

        sourceListViewFx.setOnMouseClicked(sourceListViewClickListener);
        menuSyncFx.setOnAction( event -> fullServerSync() );
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////                                                          //////////////////////////
    //                              FXML CONTROLLER METHODS                                            //
    /////////////////                                                          /////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////




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

    /**
     * Menu item for keeping local storage copy click listener
     */
    @FXML
    public void keepStorageMenuListener() {
        Main.storage = Main.Storage.TRUE;
        properties.setProperty("Local-Storage", "true");
        saveDataToFile(buildDataStringFromList(), true);
        updateProperties();
    }

    /**
     * Menu item for not keeping local storage copy click listener
     */
    @FXML
    public void noStorageMenuListener() {
        Main.storage = Main.Storage.FALSE;
        removeStorageFile();
        properties.setProperty("Local-Storage", "false");
        updateProperties();
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////                                                          //////////////////////////
    //                              PRIVATE INTERNAL METHODS                                           //
    /////////////////                                                          /////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    private String buildDataStringFromList() {

        JsonArray rootArray = new JsonArray();

        for (Source source : sourceSet) {
            JsonObject sourceObject = new JsonObject();
            sourceObject.addProperty("_id", source.getId());
            sourceObject.addProperty("userSpelledName", source.getUserSpelledName());
            sourceObject.addProperty("name", source.getName());

            JsonArray accountArray = new JsonArray();
            Iterator<Account> iterator = source.getAccounts();
            while (iterator.hasNext()){
                JsonObject accountObj = new JsonObject();
                Account account = iterator.next();
                accountObj.addProperty("username", account.getUsername());
                accountObj.addProperty("password", account.getPassword());
                accountArray.add(accountObj);
            }
            sourceObject.add("accounts", accountArray);
            rootArray.add(sourceObject);
        }

        System.out.print(rootArray.toString());

        return rootArray.toString();
    }

    private void fullServerSync() {
        //TODO figure out how to order this to account for offline changes
        serverGET();
        //serverPUT();
        //serverPOST();
        //serverDELETE();
    }

    /**
     * Performs a GET sync with server
     */
    private void serverGET() {
        //JSON body message
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Username", "Brad");
        jsonObject.addProperty("Password", "12345");
        jsonObject.addProperty("method", "GET");

        TaskService service = new TaskService(jsonObject.toString());
        service.setOnSucceeded(handleServerDataEvent);
        service.setOnCancelled(event -> {
            if (Main.server != Main.Server.OFF) {
                Main.server = Main.Server.OFF;
                if (Main.storage == Main.Storage.TRUE)
                    populateListFromStorage();
            }

            displayInformation("Error syncing with server. Please make sure server is on. We will operate in offline mode for now.");
        });
        service.start();
    }

    private void serverPUT() {
        //TODO handle server PUT
        /*String sourceName = "";
        String toChange = "";
        String username = "";
        String old = "";
        String neW = "";

        //JSON body message
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Username", "Brad");
        jsonObject.addProperty("Password", "12345");
        jsonObject.addProperty("method", "PUT");

        JsonObject restOfContent = new JsonObject();
        restOfContent.addProperty("name", sourceName);
        restOfContent.addProperty("toChange", toChange);
        restOfContent.addProperty("username", username);
        restOfContent.addProperty("old", old);
        restOfContent.addProperty("new", neW);

        jsonObject.add("restOfContent", restOfContent);

        TaskService service = new TaskService(jsonObject.toString());
        service.setOnSucceeded(handleServerDataEvent);
        service.setOnCancelled(event -> {
            if (Main.server != Main.Server.OFF) {
                Main.server = Main.Server.OFF;
                if (Main.storage == Main.Storage.TRUE)
                    populateListFromStorage();
            }

            displayInformation("Error syncing with server. Please make sure server is on. We will operate in offline mode for now.");
        });
        service.start();*/
    }

    private void serverPOST() {
        //TODO handle server POST
    }

    private void serverDELETE() {
        //TODO handle server DELETE
    }

    /**
     * Creates and adds to account list view the "Add account" add button
     */
    private void addAccountAddButton() {

        AccountAddButton accountAddButton = new AccountAddButton();
        accountStackPaneFx.getChildren().add(accountAddButton);
        StackPane.setAlignment(accountAddButton, Pos.BOTTOM_RIGHT);
    }

    /**
     * Reads PassApp.properties file and sets necessary property variables
     */
    private void setSystemSettings() {

        Map<String, String> propMap = getPropertiesFromFile();
        String current = "";

        if (propMap != null) {

            // Local storage of information property
            if ( (current = propMap.get("Local-Storage")) != null) {
                switch(current) {
                    case "true":
                        storageToggleGroup.selectToggle((Toggle)menuKeepStorageFx);
                        Main.storage = Main.Storage.TRUE;
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

            // Color theme property (Future)
            if ( (current = propMap.get("Color-Theme")) != null) {
                switch (current) {
                    case "default":
                        break;
                    default:
                }
            }
        }
    }

    /**
     * Parses properties from properties file and returns hash map of properties
     * @return HashMap<String, String> containing property key -> value pairs
     */
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

    /**
     * Populates source list from local storage
     */
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

                if (sourceSet.size() != 0) {
                    sourceSet.clear();
                    observableSourceList.clear();
                    sourceListViewFx.itemsProperty().unbind();
                }

                sourceSet = parseGetJsonToSet(rootArray);

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
     * Parses incoming Json data to a Set of Sources -> expects data to be in format
     * that the server sends data over in GET request.
     *
     * Parses data from server or from local-storage.jbs file
     * @param root
     * @return
     */
    private Set<Source> parseGetJsonToSet(JsonArray root) {

        Set<Source> localSources = new HashSet<>();

        for (int i = 0; i < root.size(); i++) {
            Source source = new Source();

            JsonObject sourceObject = root.get(i).getAsJsonObject();
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

            localSources.add(source);
        }

        return localSources;
    }

    /**
     * Updates properties file if properties were changed
     */
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
     * Displays text to information pane with OK button to clear message
     * @param info
     */
    private void displayInformation(String info) {
        informationBoxFx.getChildren().clear();

        Label informationLabel = new Label("");
        informationLabel.setText(info);
        VBox.setMargin(informationLabel, new Insets(10.0, 5.0, 5.0, 5.0));
        informationLabel.setWrapText(true);
        Button okButton = new Button("OK");
        informationBoxFx.getChildren().add(informationLabel);
        informationBoxFx.getChildren().add(okButton);


        okButton.setOnMouseClicked(event1 -> {
            informationBoxFx.getChildren().clear();
        });
    }

    /**
     * Writes to file, local-storage.jbs, all password information
     * @param data the password data to write to file (must be in format of a GET from server to be reparsed
     *             correctly) -> Array of source objects ==> [ {source1}, {source2}, ... , {sourceN} ]
     * @param doEncryption true to encrypt data before write, false if data is already encrypted and doesnt
     *                     need to be encrypted again
     */
    private void saveDataToFile(String data, boolean doEncryption) {

        File file = new File("src/resources/local-storage.jbs");
        BufferedWriter bufferedWriter;
        JBSCrypto jbsCrypto = new JBSCrypto();

        try {
            if (! file.exists()) {
                if (! file.createNewFile())
                    System.out.print("File creation failed");
            }

            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write( (doEncryption) ? jbsCrypto.encrypt(data) : data );
            bufferedWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes local-storage.jbs file from disc if 'No local storage' menu option enabled
     */
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



    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////                                                          //////////////////////////
    //                              EVENT LISTENERS                                                    //
    /////////////////                                                          /////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////



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
     *  Server GET sync data event handler
     *  Parses incoming server GET data if server online
     *  IF server offline, sets necessary flags and attempts to populate list from
     *  local storage if option enabled
     */
    private final EventHandler<WorkerStateEvent> handleServerDataEvent = event -> {

        String messageFromService = (String) event.getSource().getValue();
        JBSCrypto jbsCrypto = new JBSCrypto();

        ///////////////////////////////////////////
        //      If null then server offline     //
        /////////////////////////////////////////
        if (messageFromService != null) {

            Main.server = Main.Server.ON;

            if (Main.storage == Main.Storage.TRUE)
                saveDataToFile(messageFromService, true);

            JsonElement rootElement = new JsonParser().parse(messageFromService);
            JsonArray rootArray = rootElement.getAsJsonArray();

            if (sourceSet.size() != 0) {
                sourceSet.clear();
                observableSourceList.clear();
                sourceListViewFx.itemsProperty().unbind();
            }

            sourceSet = parseGetJsonToSet(rootArray);

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

            /*if (Main.server != Main.Server.OFF) {
                Main.server = Main.Server.OFF;
                if (Main.storage == Main.Storage.TRUE)
                    populateListFromStorage();
            }

            displayInformation("Error syncing with server. Please make sure server is on. We will operate in offline mode for now.");*/
        }
    };
}
