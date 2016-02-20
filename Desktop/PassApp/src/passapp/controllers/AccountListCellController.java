package passapp.controllers;

import brad.crypto.JBSCrypto;
import com.google.gson.JsonObject;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import passapp.Account;
import passapp.CustomOverrides.AccountDeleteButton;
import passapp.CustomOverrides.EditButton;
import passapp.DisplayMessageToUser;
import passapp.Main;
import passapp.ServerCommunication.TaskService;

/**
 * Created by Brad on 2/6/2016.
 *
 * Controller for account listview cell item fxml code
 */
public class AccountListCellController {

    ////////////////////////////////////////////////////
    ///////////////                     ///////////////
    //          FXML bound objects                 //
    ///////////                         /////////////
    ////////////////////////////////////////////////
    @FXML
    VBox accountTileVboxFx;             // VBox containing title

    @FXML
    Label accountUsernameLabelFx;       // Username label

    @FXML
    Label accountPasswordLabelFx;       // Password label

    @FXML
    StackPane accountRootStackPane;     // the root fxml object

    // Other objects
    Account account;                        // The account that is in the list cell
    ObservableList<Account> accountList;    // The observable list that populates the listview

    /**
     * Constructor
     * @param account The account that represents the listview cell item
     * @param accountList
     */
    public AccountListCellController(Account account, ObservableList<Account> accountList) {
        this.account = account;
        this.accountList = accountList;

        // Load account list view cell tile fxml code for populating
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/account-tile.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add delete button to list cell
        addDeleteAndEditButton();
    }

    /**
     *
     * @param account
     */
    public void setInfo(Account account) {
        if (accountUsernameLabelFx == null || accountPasswordLabelFx == null) {
            System.out.print("\n\nAccount is null\n\n");
        } else {
            accountUsernameLabelFx.setText(account.getUsername());
            accountPasswordLabelFx.setText("*************");
        }
    }

    public Label getPWLabel() {
        return accountPasswordLabelFx;
    }

    public StackPane getVbox() {
        return accountRootStackPane;
    }

    public void addDeleteAndEditButton() {

        AccountDeleteButton accountDelButton = new AccountDeleteButton();
        accountDelButton.setOnMouseClicked(mouseClickEvent);
        accountRootStackPane.getChildren().add(accountDelButton);
        StackPane.setAlignment(accountDelButton, Pos.TOP_RIGHT);

        EditButton editButton = new EditButton();
        editButton.setOnMouseClicked(event -> System.out.println("Edit clicked"));
        accountRootStackPane.getChildren().add(editButton);
        StackPane.setAlignment(editButton, Pos.TOP_RIGHT);
    }

    private final EventHandler<MouseEvent> mouseClickEvent = event -> {

        JsonObject message = new JsonObject();
        message.addProperty("Username", "Brad");
        message.addProperty("Password", "12345");
        message.addProperty("method", "DELETE");
        JsonObject restOfContent = new JsonObject();
        restOfContent.addProperty("name", account.getParent().getUserSpelledName());
        restOfContent.addProperty("whatToDelete", "account");
        restOfContent.addProperty("username", accountUsernameLabelFx.getText());
        message.add("restOfContent", restOfContent);

        TaskService service = new TaskService(message.toString());
        service.setOnSucceeded(e -> {
            String messageFromService = (String) e.getSource().getValue();
            JBSCrypto jbsCrypto = new JBSCrypto();

            accountList.remove(account);

            System.out.print(messageFromService);
        });
        service.setOnCancelled(event1 ->  {
            // TODO handle functionality when server is offline
            if (Main.server != Main.Server.OFF) {
                Main.server = Main.Server.OFF;
            }
            accountList.remove(account);
            DisplayMessageToUser.displayMessage("Error syncing with server. Please make sure server is on. We will operate in offline mode for now and sync with server when it comes back online.");
        });
        service.start();
    };
}
