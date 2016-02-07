package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("account-tile.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setInfo(String string) {
        String[] buf = string.split("~/~");
        accountUsernameLabelFx.setText("JRadulus " + buf[0]);
        accountPasswordLabelFx.setText("Passwordulus " + buf[1]);
    }

    public VBox getVbox() {
        return accountTileVboxFx;
    }
}
