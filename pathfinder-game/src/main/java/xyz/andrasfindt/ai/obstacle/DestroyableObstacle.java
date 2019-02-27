package xyz.andrasfindt.ai.obstacle;

import xyz.andrasfindt.ai.Destroyable;
import xyz.andrasfindt.ai.geom.Rectangle2D;
import xyz.andrasfindt.ai.geom.Vector2D;

import javax.validation.constraints.NotNull;

public abstract class DestroyableObstacle extends PlayerImageObstacle implements Destroyable {
    private int health;
    private boolean dead;

    public DestroyableObstacle(Byte[][] image, int health) {
        super(image);
        this.health = health;
    }

    public DestroyableObstacle(Byte[][] image, Rectangle2D boundingBox, int health) {
        super(image, boundingBox);
        this.health = health;
    }

    @Override
    public boolean hit(@NotNull Vector2D position) {
        if (isDead()) {
            return false;
        }
        boolean hit = super.hit(position);
        if (hit) {
            if (health-- <= 0) {
                dead = true;
            }
        }
        return hit;
    }

    @Override
    public boolean isDead() {
        return dead;
    }
}
