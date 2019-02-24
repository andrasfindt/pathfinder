package xyz.andrasfindt.ai.geom;

public class Rectangle2D {
    public final double startX;
    public final double startY;
    public final double endX;
    public final double endY;
    public final double width;
    public final double height;

    public Rectangle2D(Vector2D start, Vector2D end) {
        this.startX = start.x;
        this.startY = start.y;
        this.endX = end.x;
        this.endY = end.y;
        this.width = endX - startX;
        this.height = endY - startY;
    }

    public Rectangle2D(double x, double y, double width, double height) {
        this.startX = x;
        this.startY = y;
        this.endX = x + width;
        this.endY = y + height;
        this.width = width;
        this.height = height;
    }

    public boolean contains(Vector2D point) {
        return point.x >= startX &&
                point.y >= startY &&
                point.x <= endX &&
                point.y <= endY;
    }
}
