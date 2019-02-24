package xyz.andrasfindt.ai.obstacle;


import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.geom.Vector2D;

import java.util.Optional;

public abstract class ImageObstacle extends Obstacle {
    protected static final int W_MAX = Game.Setup.SCREEN_WIDTH - 1;
    protected static final int H_MAX = Game.Setup.SCREEN_HEIGHT - 1;
    protected Byte[][] image;

    public ImageObstacle(Byte[][] image) {
        this.image = image;
    }

    protected static int clamp(double ordinal, int min, int max) {
        return (int) Math.max(min, Math.min(ordinal, max));
    }

    protected abstract Optional<Byte> getPixelAt(Vector2D position);

    public Byte[][] getImage() {
        return image;
    }
}
