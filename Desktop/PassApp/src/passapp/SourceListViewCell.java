package passapp;

import javafx.scene.control.ListCell;
import passapp.controllers.SourceData;

/**
 * Created by Bradley on 2/9/2016.
 *
 * custom ListCell of list view for sources.
 *
 * creates SourceData object which is the controller for the fxml code for a
 * source list cell. Interfaces with controller for creating and displaying
 * custom fxml layout for listview cell
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
