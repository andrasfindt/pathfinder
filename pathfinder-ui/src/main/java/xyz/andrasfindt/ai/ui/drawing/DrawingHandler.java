package xyz.andrasfindt.ai.ui.drawing;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import xyz.andrasfindt.ai.geom.Vector2D;

public class DrawingHandler {

    private final DrawingEvent mouseEvent;

    public DrawingHandler(DrawingListener listener) {
        this.mouseEvent = new DrawingEvent();
        listener.getView().addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> {
                    Vector2D point = new Vector2D(event.getX(), event.getY());
                    mouseEvent.clear();
                    mouseEvent.add(point);
                    listener.startDraw(point, event.getButton());
                });

        listener.getView().addEventHandler(MouseEvent.MOUSE_DRAGGED,
                event -> {
                    Vector2D point = new Vector2D(event.getX(), event.getY());
                    mouseEvent.add(point);
                    listener.drawPath(point, event.getButton());
                });

        listener.getView().addEventHandler(MouseEvent.MOUSE_RELEASED, event -> listener.complete(mouseEvent, event.getButton()));
    }
}
