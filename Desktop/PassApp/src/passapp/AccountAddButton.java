package passapp;

import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
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

    public AccountAddButton() {

        this.setMaxWidth(40.0f);
        this.setMaxHeight(40.0f);
        this.setPadding(new Insets(0.0f, 10.0f, 20.0f, 0.0f));

        Circle circle = new Circle();
        circle.setRadius(30.0f);
        circle.setFill(Color.CYAN);
        circle.setEffect(new DropShadow(40.0f, 10.0f, 10.0f, Color.BLACK));
        circle.setStyle("-fx-cursor: hand");


        Line vert = new Line();
        vert.setStartX(17.0f);
        vert.setStartY(3.0f);
        vert.setEndX(17.0f);
        vert.setEndY(37.0f);
        vert.setStroke(Color.WHITE);
        vert.setStrokeWidth(10.0f);
        vert.setId("#vertRect");
        vert.setStyle("-fx-cursor: hand");

        Line horz = new Line();
        horz.setStartX(3.0f);
        horz.setStartY(17.0f);
        horz.setEndX(37.0f);
        horz.setEndY(17.0f);
        horz.setStroke(Color.WHITE);
        horz.setStrokeWidth(10.0f);
        horz.setStyle("-fx-cursor: hand");

        setMouseEvents(circle, vert, horz);

        this.getChildren().add(circle);
        this.getChildren().add(vert);
        this.getChildren().add(horz);
    }

    /**
     * Add mouse hover events for account add button art
     *
     * @param circle - circle of add button
     * @param vert - vertical line of plus sign
     * @param horz - horizontal line of plus sign
     */
    private void setMouseEvents(Circle circle, Line vert, Line horz) {

        circle.setOnMouseEntered( event -> {
            circle.setFill(Color.CYAN.darker());
            vert.setStrokeWidth(13.0f);
            horz.setStrokeWidth(13.0f);
        });

        circle.setOnMouseExited( event -> {
            circle.setFill(Color.CYAN);
            vert.setStrokeWidth(10.0f);
            horz.setStrokeWidth(10.0f);
        });

        horz.setOnMouseEntered( event -> {
            circle.setFill(Color.CYAN.darker());
            vert.setStrokeWidth(13.0f);
            horz.setStrokeWidth(13.0f);
        });

        horz.setOnMouseExited( event -> {
            circle.setFill(Color.CYAN);
            vert.setStrokeWidth(10.0f);
            horz.setStrokeWidth(10.0f);
        });

        vert.setOnMouseEntered( event -> {
            circle.setFill(Color.CYAN.darker());
            vert.setStrokeWidth(13.0f);
            horz.setStrokeWidth(13.0f);
        });

        vert.setOnMouseExited( event -> {
            circle.setFill(Color.CYAN);
            vert.setStrokeWidth(10.0f);
            horz.setStrokeWidth(10.0f);
        });
    }
}
