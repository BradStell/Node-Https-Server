package passapp;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import passapp.controllers.AccountData;

/**
 * Created by Bradley on 2/9/2016.
 */
public class AccountListViewCell extends ListCell<Account> {

    Account cellItem;
    AccountData accountData;

    @Override
    protected void updateItem(Account item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            cellItem = item;
            accountData = new AccountData();
            accountData.setInfo(item);
            setGraphic(accountData.getVbox());

            this.setOnMouseEntered(mouseEnteredEvent);
            this.setOnMouseExited(mouseExitedEvent);

        } else {
            setGraphic(null);
            this.setOnMouseEntered(null);
        }
    }

    private final EventHandler<MouseEvent> mouseEnteredEvent = event -> {
        Label accountPW = accountData.getPWLabel();
        accountPW.setText(cellItem.getPassword());
    };

    private final EventHandler<MouseEvent> mouseExitedEvent = event -> {
        Label accountPW = accountData.getPWLabel();
        accountPW.setText("*************");
    };

}
