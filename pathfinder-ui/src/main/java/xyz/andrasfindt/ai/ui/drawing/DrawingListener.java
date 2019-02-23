package xyz.andrasfindt.ai.ui.drawing;

import javafx.scene.input.MouseButton;
import xyz.andrasfindt.ai.geom.Vector2D;

public interface DrawingListener {
    DrawingViewWrapper getView();

    void startDraw(Vector2D point, MouseButton button);

    void drawPath(Vector2D point, MouseButton button);

    void complete(DrawingEvent mouseEvent, MouseButton button);
}
