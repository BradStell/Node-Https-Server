package passapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import passapp.Account;

/**
 * Created by Brad on 2/6/2016.
 */
public class AccountData {

    @FXML
    VBox accountTileVboxFx;

    @FXML
    Label accountUsernameLabelFx;

    @FXML
    Label accountPasswordLabelFx;

    public AccountData() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/account-tile.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public VBox getVbox() {
        return accountTileVboxFx;
    }
}
