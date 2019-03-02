package xyz.andrasfindt.ai.creep;

import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.Listener;
import xyz.andrasfindt.ai.RandomUtil;
import xyz.andrasfindt.ai.Viewable;
import xyz.andrasfindt.ai.geom.Vector2D;
import xyz.andrasfindt.ai.obstacle.Obstacle;
import xyz.andrasfindt.ai.obstacle.ObstacleStrategy;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseCreep implements Serializable {
    protected static final double DEFAULT_RADIUS = 2d;
    protected static final double GOAL_DELTA = DEFAULT_RADIUS ;
    public int genomeSize;
    protected Genome genome;
    protected boolean dead = false;
    protected Vector2D position = new Vector2D(Game.Setup.SCREEN_WIDTH / 2d, Game.Setup.SCREEN_HEIGHT - 10d);
    protected Vector2D velocity = Vector2D.ZERO;
    protected Vector2D acceleration = Vector2D.ZERO;
    protected boolean reachedGoal = false;
    protected ObstacleStrategy strategy = ObstacleStrategy.DIE_ON_HIT;
    protected ViewModel viewModel = makeViewModel();
    protected ViewModel.Paint color = new ViewModel.Paint(0.0f, 1f, 1f);
    private boolean best = false;
    private double fitness = 0d;

    protected BaseCreep(int genomeSize) {
        this.genomeSize = genomeSize;
        genome = new Genome(genomeSize);
    }

    protected BaseCreep(int genomeSize, ObstacleStrategy strategy) {
        this(genomeSize);
        this.strategy = strategy;
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D vector2D) {
        this.position = vector2D;
    }

    public void draw(Listener listener) {
//        listener.draw(position, best);
        viewModel.update(this);
        viewModel.setPaint(color);
//        listener.draw(this);
        listener.draw(viewModel);
    }

    protected abstract ViewModel makeViewModel();

    void move() {
        if (genome.genomeSize > genome.step) {
            acceleration = genome.genes[genome.step];
            genome.step++;
        } else {
            dead = true;
        }
        velocity = velocity.add(acceleration);
        velocity = velocity.limit(getSpeedLimit());
        position = position.add(velocity);
    }

    protected abstract double getSpeedLimit();

    public void update() {
        if (!dead && !reachedGoal) {
            move();
            if (position.distance(Game.Setup.goal) < GOAL_DELTA) {//if reached goal
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

    protected boolean isOutOfBounds() {
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
//                takeHit(obstacle);
                break;
            }
        }
        return hit;
    }

    //fixme
    // create new fitness function.
    public void calculateFitness() {
        if (reachedGoal) {      //10000d originally. not sure what difference it makes. Really need to try give a shit about the number
            fitness = 1d / 16d + 10000d / (genome.step * genome.step);
        } else {
            double d = position.distance(Game.Setup.goal);
            fitness = 1d / (d * d);
        }
    }

    public BaseCreep makeChild() {
        BaseCreep child = makeNew();
        try {
            child.genome = genome.copy();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        child.strategy = strategy;
        return child;
    }

    protected abstract BaseCreep makeNew();

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

    public abstract static class ViewModel<T extends BaseCreep> implements Viewable {
        private UUID id = RandomUtil.nextUUID();
        private int genomeSize;
        private boolean dead;
        private Vector2D position;
        private Vector2D velocity;
        private Vector2D acceleration;
        private boolean reachedGoal;
        private boolean best;
        private double fitness;
        private ObstacleStrategy strategy;
        private double radius = DEFAULT_RADIUS;
        private Paint paint = new Paint(0d, 1d, 1d);

        ViewModel(T creep) {
            update(creep);
        }

        public double getRadius() {
            return radius;
        }

        public void setRadius(double radius) {
            this.radius = radius;
        }

        public Paint getPaint() {
            return paint;
        }

        public void setPaint(Paint paint) {
            this.paint = paint;
        }

        public int getGenomeSize() {
            return genomeSize;
        }

        public void setGenomeSize(int genomeSize) {
            this.genomeSize = genomeSize;
        }

        public boolean isDead() {
            return dead;
        }

        public void setDead(boolean dead) {
            this.dead = dead;
        }

        public Vector2D getPosition() {
            return position;
        }

        public void setPosition(Vector2D position) {
            this.position = position;
        }

        public Vector2D getVelocity() {
            return velocity;
        }

        public void setVelocity(Vector2D velocity) {
            this.velocity = velocity;
        }

        public Vector2D getAcceleration() {
            return acceleration;
        }

        public void setAcceleration(Vector2D acceleration) {
            this.acceleration = acceleration;
        }

        public boolean isReachedGoal() {
            return reachedGoal;
        }

        public void setReachedGoal(boolean reachedGoal) {
            this.reachedGoal = reachedGoal;
        }

        public boolean isBest() {
            return best;
        }

        public void setBest(boolean best) {
            this.best = best;
        }

        public double getFitness() {
            return fitness;
        }

        public void setFitness(double fitness) {
            this.fitness = fitness;
        }

        public ObstacleStrategy getStrategy() {
            return strategy;
        }

        public void setStrategy(ObstacleStrategy strategy) {
            this.strategy = strategy;
        }

        public void update(T creep) {
            this.genomeSize = creep.genomeSize;
            this.acceleration = creep.acceleration.copy();
            this.dead = creep.dead;
            this.position = creep.position.copy();
            this.velocity = creep.velocity.copy();
            this.reachedGoal = creep.reachedGoal;
            this.best = creep.isBest();
            this.fitness = creep.getFitness();
            this.strategy = creep.strategy;
        }

        public UUID getId() {
            return id;
        }

        public static class Paint {
            private double hue;
            private double saturation;
            private double brightness;

            public Paint(double hue, double saturation, double brightness) {
                this.hue = hue;
                this.saturation = saturation;
                this.brightness = brightness;
            }

            public double getHue() {
                return hue;
            }

            public void setHue(double hue) {
                this.hue = hue;
            }

            public double getSaturation() {
                return saturation;
            }

            public void setSaturation(double saturation) {
                this.saturation = saturation;
            }

            public double getBrightness() {
                return brightness;
            }

            public void setBrightness(double brightness) {
                this.brightness = brightness;
            }
        }
    }
}
