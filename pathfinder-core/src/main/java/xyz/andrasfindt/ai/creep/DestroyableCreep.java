package xyz.andrasfindt.ai.creep;

import xyz.andrasfindt.ai.Destroyable;
import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.obstacle.Obstacle;
import xyz.andrasfindt.ai.obstacle.ObstacleStrategy;

public abstract class DestroyableCreep extends BaseCreep implements Destroyable {
    protected int health;

    protected DestroyableCreep(int genomeSize, int health) {
        super(genomeSize, ObstacleStrategy.BOUNCE);
        this.health = health;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    protected abstract void takeHit(Obstacle obstacle);

    @Override
    public void update() {
        if (!dead && !reachedGoal) {
            move();
            if (position.distance(Game.Setup.goal) < 5d) {//if reached goal
                reachedGoal = true;
            } else if (isOutOfBounds() || hasHitObstacle()) {
                switch (strategy) {
                    case DIE_ON_HIT:
                        takeHit(null);
                        dead = true;
                        break;
                    case BOUNCE:
                        velocity = velocity.multiply(-1); // "bounce" off walls
                        position = position.add(velocity);
                        takeHit(null);
                        break;
                    default:
                        throw new RuntimeException("not implemented");
                }
            }
        }
    }
}
