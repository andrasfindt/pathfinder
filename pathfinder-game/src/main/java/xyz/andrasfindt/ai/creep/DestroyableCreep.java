package xyz.andrasfindt.ai.creep;

import xyz.andrasfindt.ai.Destroyable;
import xyz.andrasfindt.ai.internal.BaseCreep;
import xyz.andrasfindt.ai.obstacle.ObstacleStrategy;

public abstract class DestroyableCreep extends BaseCreep implements Destroyable {
    int health;

    DestroyableCreep(int genomeSize, int health) {
        super(genomeSize, ObstacleStrategy.BOUNCE);
        this.health = health;
    }

    @Override
    public boolean isDead() {
        return dead;
    }
}
