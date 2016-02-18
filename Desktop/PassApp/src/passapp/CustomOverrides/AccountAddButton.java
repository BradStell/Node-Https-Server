package passapp.CustomOverrides;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Created by Brad on 2/10/2016.
 *
 * This is the circular button with a plus sign on it for adding new accounts
 */
public class AccountAddButton extends StackPane {

    Circle circle;
    Line vert;
    Line horz;

    public AccountAddButton() {

        this.setMaxWidth(40.0f);
        this.setMaxHeight(40.0f);
        this.setPadding(new Insets(0.0f, 10.0f, 20.0f, 0.0f));

        circle = new Circle();
        circle.setRadius(30.0f);
        circle.setFill(Color.CYAN);
        circle.setEffect(new DropShadow(10.0f, -2.0f, 2.0f, Color.rgb(30, 30, 30)));
        circle.setStyle("-fx-cursor: hand");


        vert = new Line();
        vert.setStartX(17.0f);
        vert.setStartY(3.0f);
        vert.setEndX(17.0f);
        vert.setEndY(37.0f);
        vert.setStroke(Color.WHITE);
        vert.setStrokeWidth(10.0f);
        vert.setId("#vertRect");
        vert.setStyle("-fx-cursor: hand");

        horz = new Line();
        horz.setStartX(3.0f);
        horz.setStartY(17.0f);
        horz.setEndX(37.0f);
        horz.setEndY(17.0f);
        horz.setStroke(Color.WHITE);
        horz.setStrokeWidth(10.0f);
        horz.setStyle("-fx-cursor: hand");

        setMouseHoverEvents();

        this.getChildren().add(circle);
        this.getChildren().add(vert);
        this.getChildren().add(horz);
    }

    /**
     * Add mouse hover events for account add button art
     *
     */
    private void setMouseHoverEvents() {

        circle.setOnMouseEntered(mouseEnterEvent);
        circle.setOnMouseExited(mouseExitEvent);

        horz.setOnMouseEntered(mouseEnterEvent);
        horz.setOnMouseExited(mouseExitEvent);

        vert.setOnMouseEntered(mouseEnterEvent);
        vert.setOnMouseExited(mouseExitEvent);
    }

    private final EventHandler<MouseEvent> mouseEnterEvent = event -> {
        circle.setFill(Color.CYAN.darker());
        vert.setStrokeWidth(13.0f);
        horz.setStrokeWidth(13.0f);
    };

    private final EventHandler<MouseEvent> mouseExitEvent = event -> {
        circle.setFill(Color.CYAN);
        vert.setStrokeWidth(10.0f);
        horz.setStrokeWidth(10.0f);
    };
}
