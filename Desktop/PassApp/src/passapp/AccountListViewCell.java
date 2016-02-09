package passapp;

import javafx.scene.control.ListCell;
import passapp.controllers.AccountData;

/**
 * Created by Bradley on 2/9/2016.
 */
public class AccountListViewCell extends ListCell<Account> {

    @Override
    protected void updateItem(Account item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            AccountData accountData = new AccountData();
            accountData.setInfo(item);
            setGraphic(accountData.getVbox());
        }
    }
}
