package sample;

import javafx.scene.control.ListCell;

/**
 * Created by Brad on 2/6/2016.
 */
public class ListViewCell extends ListCell<String> {

    String source = "";

    public ListViewCell(String source) {
        this.source = source;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        switch (source) {
            case "source":
                if (item != null) {
                    SourceData sourceData = new SourceData();
                    sourceData.setInfo(item);
                    setGraphic(sourceData.getHbox());
                }
                break;
            case "account":
                if (item != null) {
                    AccountData accountData = new AccountData();
                    accountData.setInfo(item);
                    setGraphic(accountData.getVbox());
                }
                break;
        }
    }
}
