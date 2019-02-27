package xyz.andrasfindt.ai.internal;

import xyz.andrasfindt.ai.obstacle.Obstacle;

public class TestCreep extends BaseCreep {
    protected TestCreep(int genomeSize) {
        super(genomeSize);
    }

    @Override
    protected void takeHit(Obstacle obstacle) {

    }

    @Override
    protected BaseCreep makeNew() {
        return new TestCreep(genomeSize);
    }
}
