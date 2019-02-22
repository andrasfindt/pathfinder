package xyz.andrasfindt.ai.geom;

public class Vector2D {

    public static final Vector2D ZERO = new Vector2D(0d, 0d);

    public final double x;
    public final double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2D fromAngle(double angle) {
        return new Vector2D(Math.cos(angle), Math.sin(angle));
    }

    public double distance(double x, double y) {
        return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
    }

    public double distance(Vector2D point) {
        return distance(point.x, point.y);
    }

    public Vector2D add(double x, double y) {
        return new Vector2D(this.x + x, this.y + y);
    }

    public Vector2D add(Vector2D point) {
        return add(point.x, point.y);
    }

    public Vector2D subtract(double x, double y) {
        return new Vector2D(this.x - x, this.y - y);
    }

    public Vector2D subtract(Vector2D point) {
        return subtract(point.x, point.y);
    }

    public Vector2D multiply(double factor) {
        return new Vector2D(x * factor, y * factor);
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D unitVector() {
        double magnitude = magnitude();
        if (magnitude() == 0d) {
            return ZERO;
        }
        if (magnitude != 1d) {
            double x = this.x / magnitude;
            double y = this.y / magnitude;
            return new Vector2D(x, y);
        }
        return limit(1d);
    }

    public Vector2D limit(double limit) {
        double magnitude = magnitude();
        if (magnitude > limit) {
            double x = this.x / magnitude * limit;
            double y = this.y / magnitude * limit;
            return new Vector2D(x, y);
        }
        return this;
    }

    public Vector2D copy() {
        return new Vector2D(x, y);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Vector2D)) {
            return false;
        }
        Vector2D v = (Vector2D) obj;
        return x == v.x && y == v.y;
    }

    public static class DoubleUtil {
        public static double distance(double x0, double y0, double x1, double y1) {
            return Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
        }
    }
}
