package xyz.andrasfindt.ai.obstacle;

import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.geom.Rectangle2D;
import xyz.andrasfindt.ai.geom.Vector2D;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class PlayerImageObstacle extends ImageObstacle {
    private final Rectangle2D boundingBox;

    public PlayerImageObstacle(Byte[][] image) {
        super(image);
        this.boundingBox = new Rectangle2D(0d, 0d, W_MAX, H_MAX);
    }

    public PlayerImageObstacle(Byte[][] image, Rectangle2D boundingBox) {
        super(shrinkToBoundingBox(image, boundingBox));
        this.boundingBox = boundingBox;
    }

    private static Byte[][] shrinkToBoundingBox(Byte[][] image, Rectangle2D boundingBox) {
        int width = (int) boundingBox.width;
        int height = (int) boundingBox.height;
        Byte[][] bytes = new Byte[width][height];
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                bytes[w][h] = image[(int) (boundingBox.start.x + w)][(int) (boundingBox.start.y + h)];
            }
        }
        return bytes;
    }

    public Rectangle2D getBoundingBox() {
        return boundingBox;
    }

    @Override
    protected Optional<Byte> getPixelAt(Vector2D position) {
        if (boundingBox.contains(position)) {
//            int x = clamp(position.x, (int) boundingBox.startX, (int) boundingBox.endX);
            int x = clamp(position.x - boundingBox.start.x, 0, (int) boundingBox.width);
//            int y = clamp(position.y, (int) boundingBox.startY, (int) boundingBox.endY);
            int y = clamp(position.y - boundingBox.start.y, 0, (int) boundingBox.height);
            return Optional.of(image[x][y]);
        } else {
            return Optional.empty();
        }
    }

    public boolean hit(@NotNull Vector2D position) {
        return getPixelAt(position).orElse(Byte.MAX_VALUE) <= Game.Setup.COLLISION_THRESHOLD;
    }

}
