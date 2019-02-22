package xyz.andrasfindt.ai.obstacle;


import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.geom.Vector2D;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class ImageObstacle extends Obstacle {
    public static final int W_MAX = Game.Setup.SCREEN_WIDTH - 1;
    public static final int H_MAX = Game.Setup.SCREEN_HEIGHT - 1;
    private Byte[][] image;

    public ImageObstacle(Byte[][] image) {
        this.image = image;
    }

    @Override
    public boolean hit(@NotNull Vector2D position) {
        return getPixelAt(position).orElse(Byte.MAX_VALUE) <= Game.Setup.COLLISION_THRESHOLD;
    }

    private Optional<Byte> getPixelAt(Vector2D position) {
        return Optional.of(image[clamp(position.x, W_MAX)][clamp(position.y, H_MAX)]);
    }

    private int clamp(double ordinal, int max) {
        return (int) Math.max(0, Math.min(ordinal, max));
    }

    public Byte[][] getImage() {
        return image;
    }
}
