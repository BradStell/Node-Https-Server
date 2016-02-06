package sample;

import javafx.scene.control.ListCell;

/**
 * Created by Brad on 2/6/2016.
 */
public class ListViewCell extends ListCell<String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            Data data = new Data();
            data.setInfo(item);
            setGraphic(data.getVbox());
        }
    }
}
