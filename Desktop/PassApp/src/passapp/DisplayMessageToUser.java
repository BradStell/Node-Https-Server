package passapp;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Created by Brad on 2/18/2016.
 */
public abstract class DisplayMessageToUser {

    private static VBox informationBoxFx;

    public static void bindUIComponents(VBox rootPane) {
        informationBoxFx = rootPane;
    }

    public static void displayMessage(String message) {

        informationBoxFx.getChildren().clear();

        Label informationLabel = new Label("");
        informationLabel.setText(message);
        VBox.setMargin(informationLabel, new Insets(10.0, 5.0, 5.0, 5.0));
        informationLabel.setWrapText(true);
        Button okButton = new Button("OK");
        informationBoxFx.getChildren().add(informationLabel);
        informationBoxFx.getChildren().add(okButton);


        okButton.setOnMouseClicked(event1 -> {
            informationBoxFx.getChildren().clear();
        });
    }
}
