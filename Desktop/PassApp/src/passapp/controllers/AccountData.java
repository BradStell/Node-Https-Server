package passapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import passapp.Account;
import passapp.CustomOverrides.AccountDeleteButton;

/**
 * Created by Brad on 2/6/2016.
 *
 * Controller for account listview cell fxml code
 */
public class AccountData {

    @FXML
    VBox accountTileVboxFx;

    @FXML
    Label accountUsernameLabelFx;

    @FXML
    Label accountPasswordLabelFx;

    @FXML
    StackPane accountRootStackPane;

    public AccountData() {
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
        accountRootStackPane.getChildren().add(accountDelButton);
        StackPane.setAlignment(accountDelButton, Pos.TOP_RIGHT);
    }
}
