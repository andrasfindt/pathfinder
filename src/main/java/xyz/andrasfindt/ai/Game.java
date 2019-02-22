package xyz.andrasfindt.ai;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    public static long RANDOM_SEED = System.currentTimeMillis();
    public static int POPULATION_SIZE = 1000;
    public static double MUTATION_RATE = 0.01d;
    public static final int COLLISION_THRESHOLD = 63;
    public static double SPEED_LIMIT = 5d;
    public static final int SCREEN_WIDTH = 512;
    public static final int SCREEN_HEIGHT = 512;
    private static final double GOAL_X = SCREEN_WIDTH / 2d;
    private static final double GOAL_Y = 10d;
    public static final Vector2D goal = new Vector2D(GOAL_X, GOAL_Y);
    static Vector2D screenSize = new Vector2D((double) SCREEN_WIDTH, (double) SCREEN_HEIGHT);
    private static List<Obstacle> obstacles = getObstacles();

    public static List<Obstacle> getObstacles() {
        if (obstacles == null) {
            obstacles = new ArrayList<>();
        }
        return obstacles;
    }

    public static void setExclusiveImageObstacle(@NotNull ImageObstacle imageObstacle) {
        obstacles = obstacles.stream().filter(o -> !(o instanceof ImageObstacle)).collect(Collectors.toList());
        obstacles.add(imageObstacle);
    }


    public static List<ImageObstacle> getImageObstacles() {
        return obstacles.stream().filter(o -> (o instanceof ImageObstacle)).map(o -> (ImageObstacle) o).collect(Collectors.toList());
    }
}