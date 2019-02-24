package xyz.andrasfindt.ai.obstacle;

import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.geom.Vector2D;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class BackgroundImageObstacle extends ImageObstacle {
    public BackgroundImageObstacle(Byte[][] image) {
        super(image);
    }

    @Override
    protected Optional<Byte> getPixelAt(Vector2D position) {
        return Optional.of(image[clamp(position.x, 0, W_MAX)][clamp(position.y, 0, W_MAX)]);
    }

    @Override
    public boolean hit(@NotNull Vector2D position) {
        return getPixelAt(position).orElse(Byte.MAX_VALUE) <= Game.Setup.COLLISION_THRESHOLD;
    }
}
