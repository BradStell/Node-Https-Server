package passapp.CustomOverrides;

import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Created by Brad on 2/13/2016.
 */
public class AccountDeleteButton extends StackPane {

    public AccountDeleteButton() {

        this.setMaxWidth(10.0f);
        this.setMaxHeight(10.0f);
        this.setPadding(new Insets(10.0f, 10.0f, 0.0f, 0.0f));

        Circle circle = new Circle();
        circle.setRadius(10.0f);
        circle.setFill(Color.RED);
        circle.setEffect(new DropShadow(10.0f, 5.0f, 5.0f, Color.BLACK));
        circle.setStyle("-fx-cursor: hand");

        Line vert = new Line();
        vert.setStartX(0.5f);
        vert.setStartY(0.5f);
        vert.setEndX(9.5f);
        vert.setEndY(9.5f);
        vert.setStroke(Color.WHITE);
        vert.setStrokeWidth(1.0f);
        vert.setStyle("-fx-cursor: hand");

        Line horz = new Line();
        horz.setStartX(9.5f);
        horz.setStartY(0.5f);
        horz.setEndX(0.5f);
        horz.setEndY(9.5f);
        horz.setStroke(Color.WHITE);
        horz.setStrokeWidth(1.0f);
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
            circle.setFill(Color.RED.darker());
            vert.setStrokeWidth(1.5f);
            horz.setStrokeWidth(1.5f);
        });

        circle.setOnMouseExited( event -> {
            circle.setFill(Color.RED);
            vert.setStrokeWidth(1.0f);
            horz.setStrokeWidth(1.0f);
        });

        horz.setOnMouseEntered( event -> {
            circle.setFill(Color.RED.darker());
            vert.setStrokeWidth(1.5f);
            horz.setStrokeWidth(1.5f);
        });

        horz.setOnMouseExited( event -> {
            circle.setFill(Color.RED);
            vert.setStrokeWidth(1.0f);
            horz.setStrokeWidth(1.0f);
        });

        vert.setOnMouseEntered( event -> {
            circle.setFill(Color.RED.darker());
            vert.setStrokeWidth(1.5f);
            horz.setStrokeWidth(1.5f);
        });

        vert.setOnMouseExited( event -> {
            circle.setFill(Color.RED);
            vert.setStrokeWidth(1.0f);
            horz.setStrokeWidth(1.0f);
        });
    }
}
