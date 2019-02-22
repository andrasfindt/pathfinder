package xyz.andrasfindt.ai.ui.drawing;

import xyz.andrasfindt.ai.geom.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class DrawingEvent {
    private List<Vector2D> points = new ArrayList<>();

    public void clear() {
        points.clear();
    }

    public void add(Vector2D point) {
        points.add(point);
    }

    public Vector2D getHead() {
        return points.get(0);
    }

    public List<Vector2D> getRemainder() {
        return points.subList(1, points.size() - 1);
    }

    public boolean isClickEvent() {
        return points.size() == 1;
    }
}
