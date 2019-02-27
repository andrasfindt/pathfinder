package xyz.andrasfindt.ai.creep;

import xyz.andrasfindt.ai.internal.BaseCreep;
import xyz.andrasfindt.ai.obstacle.Obstacle;

public class BasicCreep extends DestroyableCreep {

    private static final int DEFAULT_HEALTH = 1;

    public BasicCreep(int genomeSize) {
        super(genomeSize, DEFAULT_HEALTH);
    }

    @Override
    protected BaseCreep makeNew() {
        return new BasicCreep(genomeSize);
    }

    @Override
    protected void takeHit(Obstacle obstacle) {
        if (health-- <= 0) {
            dead = true;
        }

    }
}
