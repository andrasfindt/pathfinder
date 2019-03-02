package xyz.andrasfindt.ai.creep;

import xyz.andrasfindt.ai.Destroyable;
import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.obstacle.Obstacle;
import xyz.andrasfindt.ai.obstacle.ObstacleStrategy;

public abstract class DestroyableCreep extends BaseCreep implements Destroyable {
    private static final double GREEN_HUE = 120d;
    private static final double GREEN_HUE_SCALING_FACTOR_B = GREEN_HUE / (Game.Setup.goal.distance(Game.Setup.screenSize));
    private final double GREEN_HUE_SCALING_FACTOR = GREEN_HUE / (Game.Setup.SCREEN_HEIGHT - Game.Setup.goal.y / 2d - 1d);
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

    private double getHue(double distance) {
        //fixme
        // this calculates the color based on the current location.
        // Perhaps we can have each dot base its color on some feature of its genome (a hash of sorts of the first gene?
        //  --- not currently available)

        //todo
        // in future the plan for this should be to calculate the value based on the distance to the current goal for
        // the population.
        //  --- currently this is only based on the vertical distance to the goal.

        return GREEN_HUE - distance * GREEN_HUE_SCALING_FACTOR_B;
    }

    @Override
    public void update() {
        if (!dead && !reachedGoal) {
            double distance = position.distance(Game.Setup.goal);
            move();
            if (distance < GOAL_DELTA) {//if reached goal
                reachedGoal = true;
            } else if (isOutOfBounds() || hasHitObstacle()) {
                switch (strategy) {
                    case DIE_ON_HIT:
                        takeHit(null);
                        dead = true;
                        break;
                    case BOUNCE:
                        takeHit(null);
                        if (!dead) {
                            velocity = velocity.multiply(-1); // "bounce" off walls
                            position = position.add(velocity);
                        }
                        break;
                    default:
                        throw new RuntimeException("not implemented");
                }
            }
            viewModel.update(this);
            this.color = new BaseCreep.ViewModel.Paint(getHue(distance), 1f, 1f);
        }
    }

    @Override
    protected double getSpeedLimit() {
        return Game.Setup.SPEED_LIMIT;
    }

    public abstract class ViewModel<T extends DestroyableCreep> extends BaseCreep.ViewModel<T> {
        protected int health;

        ViewModel(T creep) {
            super(creep);
            this.health = creep.health;
        }

        @Override
        public void update(T creep) {
            super.update(creep);
            this.health = creep.health;
        }

        public int getHealth() {
            return health;
        }
    }
}
