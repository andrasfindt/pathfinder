package xyz.andrasfindt.ai;

import java.awt.geom.Rectangle2D;

public class RectangleObstacle extends Obstacle implements Rectangle {

    private final Vector2D start;
    private final Vector2D end;

    public RectangleObstacle(Vector2D start, Vector2D end) {
        this.start = start;
        this.end = end;
    }

    @Override
    boolean hit(Vector2D position) {
        boolean greaterThanStartX = position.x > start.x;
        boolean lessThanEndX = position.x < end.x;
        boolean greaterThanStartY = position.y > start.y;
        boolean lessThanEndY = position.y < end.y;

        return greaterThanStartX && lessThanEndX && greaterThanStartY && lessThanEndY;
    }

    @Override
    public Rectangle2D.Double asDoubleRectangle() {
        double width = end.x - start.x;
        double height = end.y - start.y;
        return new Rectangle2D.Double(start.x, start.y, width, height);
    }
}
