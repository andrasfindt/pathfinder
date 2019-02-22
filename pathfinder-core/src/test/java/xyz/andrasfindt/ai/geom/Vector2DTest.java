package xyz.andrasfindt.ai.geom;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class Vector2DTest {
    @Test
    public void testLimit() {
        Vector2D point = new Vector2D(10d, 10d);
        System.out.println(point);
        System.out.println(point.magnitude());
        point = point.limit(5d);
        assertEquals(5, point.magnitude(), 0.0000001);
        System.out.println(point);
        System.out.println(point.magnitude());
    }

    @Test
    public void testUnitVectorFromABigOne() {
        Vector2D point = new Vector2D(10d, 10d);
        System.out.println(point);
        System.out.println(point.magnitude());
        point = point.unitVector();
        assertEquals(1, point.magnitude(), 0.0000001);
        System.out.println(point);
        System.out.println(point.magnitude());
    }

    @Test
    public void testUnitVectorFromABooper() {
        Vector2D point = new Vector2D(.5d, .5d);
        System.out.println(point);
        System.out.println(point.magnitude());
        point = point.unitVector();
        assertEquals(1, point.magnitude(), 0.0000001);
        System.out.println(point);
        System.out.println(point.magnitude());
    }

    @Test
    public void testDistance() {
        Vector2D point = new Vector2D(0d, 1d);
        System.out.println(point);
        double x = 5.0d;
        double y = 1.0d;
        System.out.printf("[%s, %s]\n", x, y);
        double distance = point.distance(x, y);
        assertEquals(5, distance, 0.0000001);
        System.out.println(distance);
        Vector2D point1 = new Vector2D(x, y);
        System.out.println(point1);
        distance = point.distance(point1);
        assertEquals(5, distance, 0.0000001);
        System.out.println(distance);
    }

    @Test
    public void fromAngle() {
        Vector2D vector2D = Vector2D.fromAngle(0d);
        System.out.println(vector2D);
        assertEquals("[1.0, 0.0]", vector2D.toString());

        vector2D = Vector2D.fromAngle(0.01d);
        System.out.println(vector2D);
        assertEquals("[0.9999500004166653, 0.009999833334166664]", vector2D.toString());
    }
}