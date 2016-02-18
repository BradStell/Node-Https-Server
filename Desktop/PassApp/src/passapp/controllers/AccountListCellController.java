package passapp.controllers;

import brad.crypto.JBSCrypto;
import com.google.gson.JsonObject;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import passapp.Account;
import passapp.CustomOverrides.AccountDeleteButton;
import passapp.DisplayMessageToUser;
import passapp.Main;
import passapp.ServerCommunication.TaskService;

/**
 * Created by Brad on 2/6/2016.
 *
 * Controller for account listview cell fxml code
 */
public class AccountListCellController {

    @FXML
    VBox accountTileVboxFx;

    @FXML
    Label accountUsernameLabelFx;

    @FXML
    Label accountPasswordLabelFx;

    @FXML
    StackPane accountRootStackPane;

    Account account;
    ObservableList<Account> accountList;

    public AccountListCellController(Account account, ObservableList<Account> accountList) {
        this.account = account;
        this.accountList = accountList;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/account-tile.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        addDeleteButton();
    }

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

    public void addDeleteButton() {

        AccountDeleteButton accountDelButton = new AccountDeleteButton();
        accountDelButton.setOnMouseClicked(mouseClickEvent);
        accountRootStackPane.getChildren().add(accountDelButton);
        StackPane.setAlignment(accountDelButton, Pos.TOP_RIGHT);
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
