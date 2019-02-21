package xyz.andrasfindt.ai;


import javax.validation.constraints.NotNull;

public class ImageObstacle extends Obstacle {
    public static final int W_MAX = Game.SCREEN_WIDTH - 1;
    public static final int H_MAX = Game.SCREEN_HEIGHT - 1;
    private byte[][] image;

    public ImageObstacle(byte[][] image) {
        this.image = image;
    }

    @Override
    boolean hit(@NotNull Vector2D position) {
        return getPixelAt(position) <= Game.COLLISION_THRESHOLD;
    }

    private byte getPixelAt(Vector2D position) {
        return image[clamp(position.x, W_MAX)][clamp(position.y, H_MAX)];
    }

    private int clamp(double ordinal, int max) {
        return (int) Math.max(0, Math.min(ordinal, max));
    }

    public byte[][] getImage() {
        return image;
    }
}
