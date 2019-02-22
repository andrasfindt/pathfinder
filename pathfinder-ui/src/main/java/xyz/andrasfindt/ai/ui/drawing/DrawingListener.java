package xyz.andrasfindt.ai.ui.drawing;

import xyz.andrasfindt.ai.geom.Vector2D;

public interface DrawingListener {
    DrawingViewWrapper getView();

    void startDraw(Vector2D point);

    void drawPath(Vector2D point);

    void complete(DrawingEvent mouseEvent);
}
