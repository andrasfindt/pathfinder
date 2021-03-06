package xyz.andrasfindt.ai.internal;

import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.Listener;
import xyz.andrasfindt.ai.geom.Vector2D;
import xyz.andrasfindt.ai.obstacle.Obstacle;
import xyz.andrasfindt.ai.obstacle.ObstacleStrategy;

public class Creep {
    private Vector2D position = new Vector2D(Game.Setup.SCREEN_WIDTH / 2d, Game.Setup.SCREEN_HEIGHT - 10d);
    private Vector2D velocity = Vector2D.ZERO;
    private Vector2D acceleration = Vector2D.ZERO;

    private Genome genome;

    private boolean dead = false;
    private boolean reachedGoal = false;
    private boolean best = false;

    private double fitness = 0d;

    private ObstacleStrategy strategy = ObstacleStrategy.DIE_ON_HIT;
    private int genomeSize;

    Creep(int genomeSize) {
        this.genomeSize = genomeSize;
        genome = new Genome(genomeSize);
    }

    Creep(int genomeSize, ObstacleStrategy strategy) {
        this(genomeSize);
        this.strategy = strategy;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D vector2D) {
        this.position = vector2D;
    }

    public void draw(Listener listener) {
        listener.draw(position, best);
    }

    void move() {
        if (genome.genomeSize > genome.step) {
            acceleration = genome.genes[genome.step];
            genome.step++;
        } else {
            dead = true;
        }
        velocity = velocity.add(acceleration);
        velocity = velocity.limit(Game.Setup.SPEED_LIMIT);
        position = position.add(velocity);

    }

    void update() {
        if (!dead && !reachedGoal) {
            move();
            if (position.distance(Game.Setup.goal) < 5d) {//if reached goal
                reachedGoal = true;
            } else if (isOutOfBounds() || hasHitObstacle()) {
                switch (strategy) {
                    case DIE_ON_HIT:
                        dead = true;
                        break;
                    case BOUNCE:
                        velocity = velocity.multiply(-1); // "bounce" off walls
                        position = position.add(velocity);
                        break;
                    case UNDO:
                        throw new RuntimeException("not implemented");
//                        if (genome.step < genome.genomeSize) {
//                            velocity = velocity.subtract(genome.genes[genome.step]);
//                            position = position.subtract(velocity); //"reverse?"
//                        }
//                        break;
                    case COLLISION_AVOID:
                        //need old position
                        //check if new position would hit an obstacle.
                        //yes, use old position and turn 90?
                        //this shouldn't be happening in here. should be done before collision.
//                        break;
                        throw new RuntimeException("not implemented");
                    case SPAWN_NEW:
                        //on death, spawn new child with same genome up to and excluding the collision
                        throw new RuntimeException("not implemented");
//                        break;
                }
            }
        }
    }

    private boolean isOutOfBounds() {
        return position.x < 0 ||
                position.x > Game.Setup.screenSize.x ||
                position.y < 0 ||
                position.y > Game.Setup.screenSize.y;
    }

    boolean hasHitObstacle() {
        boolean hit = false;
        for (Obstacle obstacle : Game.getObstacles()) {
            hit = obstacle.hit(position);
            if (hit) {
                break;
            }
        }
        return hit;
    }

    //fixme
    // create new fitness function.
    void calculateFitness() {
        if (reachedGoal) {
            fitness = 1d / 16d + 8192d / (genome.step * genome.step);
        } else {
            double d = position.distance(Game.Setup.goal);
            fitness = 1d / (d * d);
        }
    }

    Creep makeChild() {
        Creep child = new Creep(genomeSize);
        child.genome = genome.copy();
        child.strategy = strategy;
        return child;
    }

    public boolean hasReachedGoal() {
        return reachedGoal;
    }

    public int getStepCount() {
        return genome.step;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Genome getGenome() {
        return genome;
    }

    public boolean isBest() {
        return best;
    }

    public void setBest(boolean best) {
        this.best = best;
    }
}
