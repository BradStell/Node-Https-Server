package passapp.controllers;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.net.URL;
import java.util.*;

import com.google.gson.JsonObject;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Callback;

import passapp.*;

import brad.crypto.JBSCrypto;
/**
 * How to Use my encryption module JBSCrypto
 */
/*JBSCrypto jbsCrypto = new JBSCrypto();
String encrypted = jbsCrypto.encrypt("This is what I want to encrypt");
System.out.println("\n\nEncrypted ==> " + encrypted);
String decrypted = jbsCrypto.decrypt(encrypted);
System.out.println("\n\nDecrypted ==> " + decrypted);*/

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
        addAccountAddButton();

        // Fill in test data
        setSourceListView();

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
        observableSourceList.add(new Source("new Source " + ++count));
    }

    /**
     * Edit source button mouse click listener
     */
    @FXML
    public void sourceEditClicked() {

        // This is here for testing only
        /**
         * Internet connection to password server
         *
         * Encrypts message and decrypts response
         *
         * Demonstrates a GET from our server
         */
        try {

            String url="http://127.0.0.1:3000";
            URL object=new URL(url);

            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);

            // Set header properties
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("tier1", "45r97diIj3099KpqnzlapEIv810nZaaS0");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            //JSON body message
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Username", "Brad");
            jsonObject.addProperty("Password", "12345");
            jsonObject.addProperty("method", "GET");

            //JSON body message
            //String contactJson = "{\"Username\":\"Brad\",\"Password\":\"12345\",\"method\":\"GET\"}";

            JBSCrypto jbsCrypto = new JBSCrypto();
            String encrypted = jbsCrypto.encrypt(jsonObject.toString());

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(encrypted);
            wr.flush();

            InputStream is = con.getInputStream();

            // Capture response from server and decrypt
            Scanner scanner = new Scanner(is);
            String data = "";
            while (scanner.hasNext()) {
                data += scanner.next();
            }
            String decrypted = jbsCrypto.decrypt(data);

            // Display decrypted response
            System.out.print(decrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * For Testing
     */
    private void setSourceListView() {

        // Create test data for testing
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
        accountSet.clear();
        accountListViewFx.getItems().clear();
        accountListViewFx.itemsProperty().unbind();
        observableAccountList.clear();
        accountListProperty.unbind();

        Source source = (Source) sourceListViewFx.getSelectionModel().getSelectedItem();
        Iterator<Account> iterator = source.getAccounts();

        while (iterator.hasNext()) {
            accountSet.add(iterator.next());
        }

        // observe new list and bind to list property and set to list view
        observableAccountList.setAll(accountSet);
        accountListViewFx.setItems(observableAccountList);
        accountListProperty.set(observableAccountList);
        accountListViewFx.itemsProperty().bind(accountListProperty);

        // Set cell factory callback to handle custom list cells
        accountListViewFx.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView listView) {
                return new AccountListViewCell();
            }
        });
    };
}
