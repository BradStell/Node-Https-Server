package passapp.CustomOverrides;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by Brad on 2/19/2016.
 */
public class EditButton extends StackPane {

    Circle circle;
    ImageView editView;

    public EditButton() {

        this.setMaxWidth(10.0f);
        this.setMaxHeight(10.0f);
        this.setMargin(this, new Insets(10.0f, 35.0f, 0.0f, 0.0f));

        circle = new Circle();
        circle.setRadius(10.0f);
        circle.setFill(Color.rgb(200, 200, 200));
        circle.setEffect(new DropShadow(2.0f, -1.0f, 2.0f, Color.rgb(30, 30, 30)));
        circle.setStyle("-fx-cursor: hand");

        Image editIcon = new Image("resources/images/edit.png", 15.0f, 15.0f, true, true);
        editView = new ImageView(editIcon);


        setMouseEvents();

        this.getChildren().add(circle);
        this.getChildren().add(editView);
    }

    /**
     * Add mouse hover events for account add button art
     *
     */
    private void setMouseEvents() {

        circle.setOnMouseEntered(mouseEnterEvent);
        circle.setOnMouseExited(mouseExitEvent);

        editView.setOnMouseEntered(mouseEnterEvent);
        editView.setOnMouseExited(mouseExitEvent);
    }

    private final EventHandler<MouseEvent> mouseEnterEvent = event -> {
        circle.setFill(Color.rgb(200, 200, 200).darker());
        System.out.println("Mouse Entered");
    };

    private final EventHandler<MouseEvent> mouseExitEvent = event -> {
        circle.setFill(Color.rgb(200, 200, 200));
        System.out.println("Mouse Exited");
    };
}
