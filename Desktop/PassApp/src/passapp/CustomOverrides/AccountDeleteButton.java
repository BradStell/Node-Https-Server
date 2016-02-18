package passapp.CustomOverrides;

import com.google.gson.JsonObject;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import passapp.Account;

/**
 * Created by Brad on 2/13/2016.
 */
public class AccountDeleteButton extends StackPane {

    Circle circle;
    Line vert;
    Line horz;

    public AccountDeleteButton() {

        this.setMaxWidth(10.0f);
        this.setMaxHeight(10.0f);
        this.setPadding(new Insets(10.0f, 10.0f, 0.0f, 0.0f));

        circle = new Circle();
        circle.setRadius(10.0f);
        circle.setFill(Color.RED);
        circle.setEffect(new DropShadow(2.0f, -1.0f, 2.0f, Color.rgb(30, 30, 30)));
        circle.setStyle("-fx-cursor: hand");

        vert = new Line();
        vert.setStartX(0.5f);
        vert.setStartY(0.5f);
        vert.setEndX(9.5f);
        vert.setEndY(9.5f);
        vert.setStroke(Color.WHITE);
        vert.setStrokeWidth(1.0f);
        vert.setStyle("-fx-cursor: hand");

        horz = new Line();
        horz.setStartX(9.5f);
        horz.setStartY(0.5f);
        horz.setEndX(0.5f);
        horz.setEndY(9.5f);
        horz.setStroke(Color.WHITE);
        horz.setStrokeWidth(1.0f);
        horz.setStyle("-fx-cursor: hand");

        setMouseEvents();

        this.getChildren().add(circle);
        this.getChildren().add(vert);
        this.getChildren().add(horz);
    }

    /**
     * Add mouse hover events for account add button art
     *
     */
    private void setMouseEvents() {

        circle.setOnMouseEntered(mouseEnterEvent);
        circle.setOnMouseExited(mouseExitEvent);

        horz.setOnMouseEntered(mouseEnterEvent);
        horz.setOnMouseExited(mouseExitEvent);

        vert.setOnMouseEntered(mouseEnterEvent);
        vert.setOnMouseExited(mouseExitEvent);
    }

    private final EventHandler<MouseEvent> mouseEnterEvent = event -> {
        circle.setFill(Color.RED.darker());
        vert.setStrokeWidth(1.5f);
        horz.setStrokeWidth(1.5f);
    };

    private final EventHandler<MouseEvent> mouseExitEvent = event -> {
        circle.setFill(Color.RED);
        vert.setStrokeWidth(1.0f);
        horz.setStrokeWidth(1.0f);
    };
}
