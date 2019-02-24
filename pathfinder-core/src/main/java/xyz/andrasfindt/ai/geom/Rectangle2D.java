package xyz.andrasfindt.ai.geom;

public class Rectangle2D {
    public final double width;
    public final double height;
    public Vector2D start;
    public Vector2D end;

    public Rectangle2D(Vector2D start, Vector2D end) {
        this.start = start;
        this.end = end;
        this.width = end.x - start.x;
        this.height = end.y - start.y;
    }

    public Rectangle2D(double x, double y, double width, double height) {

        this.start = new Vector2D(x, y);
        this.end = new Vector2D(x + width, y + height);
        this.width = width;
        this.height = height;
    }

    public boolean contains(Vector2D point) {
        return point.x >= start.x &&
                point.y >= start.y &&
                point.x <= end.x &&
                point.y <= end.y;
    }
}
