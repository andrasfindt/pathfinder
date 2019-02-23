package xyz.andrasfindt.ai.internal;

import org.junit.Before;
import org.junit.Test;
import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.geom.Vector2D;
import xyz.andrasfindt.ai.obstacle.RectangleObstacle;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreepTest {

    private int genomeSize = 2;

    @Before
    public void setUp() throws Exception {
        Game.Setup.RANDOM_SEED = 0;
        RandomUtil.setRandomSeed(0);
    }

    @Test
    public void update() throws Exception {
        Creep creep = new Creep(genomeSize);
        creep.setPosition(new Vector2D(-2d, -2d));
        creep.update();
        assertTrue(creep.isDead());
        System.out.println(creep.getPosition());
    }

    @Test
    public void calculateFitness() throws Exception {
        Creep creep = new Creep(genomeSize);
        creep.setPosition(Vector2D.ZERO);
        creep.calculateFitness();
        System.out.println(creep.getFitness());
    }

    @Test
    public void makeChildShouldResultInIdenticalCopiesOfDirections() throws Exception {
        Creep creep = new Creep(genomeSize);
        Creep creep1 = creep.makeChild();
        assertArrayEquals(creep.getGenome().genes, creep1.getGenome().genes);
    }

    @Test
    public void hasHitObstacle() {
        Game.getObstacles().clear();
        Game.getObstacles().add(new RectangleObstacle(new Vector2D(0d, 300d), new Vector2D(600d, 310d)));
        Creep creep = new Creep(genomeSize);
        creep.setPosition(new Vector2D(300d, 305d));
        boolean b = creep.hasHitObstacle();
        assertTrue(b);
        creep.setPosition(new Vector2D(300d, 320d));
        b = creep.hasHitObstacle();
        assertFalse(b);
    }

    @Test
    public void hasHitObstacleFromUpdate() {
        Game.getObstacles().clear();
        Game.getObstacles().add(new RectangleObstacle(new Vector2D(0d, 300d), new Vector2D(600d, 310d)));
        Creep creep = new Creep(genomeSize);
        creep.setPosition(new Vector2D(400d, 305d));
        creep.update();
//        assertTrue(creep.dead);
    }

    @Test
    public void hasReachedGoalFromUpdate() {
        Creep creep = new Creep(genomeSize);
        creep.setPosition(Game.Setup.goal);
        creep.update();
        assertTrue(creep.hasReachedGoal());
    }
}