package xyz.andrasfindt.ai;

import java.util.Arrays;
import java.util.stream.IntStream;

import static java.lang.Math.PI;

class Brain {
    private static final double PI_2 = PI * 2d;
    private Vector2D[] directions;
    int step;
    int brainSize;

    Brain(int brainSize) {
        this.directions = new Vector2D[brainSize];
        this.step = 0;
        this.brainSize = brainSize;
        randomize();
    }

    private Brain(Vector2D[] directions) {
        this.directions = copyDirections(directions);
        this.step = 0;
        this.brainSize = directions.length;
    }

    private Vector2D[] copyDirections(Vector2D[] directions) {
        return Arrays.stream(directions).map(Vector2D::copy).toArray(Vector2D[]::new);
    }

    private void randomize() {
        IntStream.range(0, brainSize).forEach(this::assignRandomDirection);
    }

    Brain copy() {
        return new Brain(directions);
    }

    void mutate() {
        IntStream.range(0, directions.length).filter(i -> RandomUtil.nextDouble() < Game.MUTATION_RATE).forEach(this::assignRandomDirection);
    }

    private void assignRandomDirection(int i) {
        directions[i] = Vector2D.fromAngle(RandomUtil.nextDouble(PI_2));
    }

    public Vector2D[] getDirections() {
        return directions;
    }
}
