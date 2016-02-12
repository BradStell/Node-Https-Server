package passapp;

import javafx.scene.control.ListCell;
import passapp.controllers.SourceData;

/**
 * Created by Brad on 2/6/2016.
 */
public class SourceListViewCell extends ListCell<Source> {

    @Override
    protected void updateItem(Source item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            SourceData sourceData = new SourceData();
            sourceData.setInfo(item);
            setGraphic(sourceData.getHbox());
        } else {
            setGraphic(null);
        }
    }
}
