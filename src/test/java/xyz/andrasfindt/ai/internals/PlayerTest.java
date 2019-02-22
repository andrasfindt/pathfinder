package xyz.andrasfindt.ai.internals;

import org.junit.Before;
import org.junit.Test;
import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.geom.Vector2D;
import xyz.andrasfindt.ai.obstacles.RectangleObstacle;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerTest {

    @Before
    public void setUp() throws Exception {
        Game.Setup.RANDOM_SEED = 0;
        RandomUtil.setRandomSeed(0);
    }

    @Test
    public void update() throws Exception {
        Player player = new Player();
        player.setPosition(new Vector2D(-2d, -2d));
        player.update();
        assertTrue(player.isDead());
        System.out.println(player.getPosition());
    }

    @Test
    public void calculateFitness() throws Exception {
        Player player = new Player();
        player.setPosition(Vector2D.ZERO);
        player.calculateFitness();
        System.out.println(player.getFitness());
    }

    @Test
    public void makeChildShouldResultInIdenticalCopiesOfDirections() throws Exception {
        Player player = new Player();
        Player player1 = player.makeChild();
        assertArrayEquals(player.getGenome().genes, player1.getGenome().genes);
    }

    @Test
    public void hasHitObstacle() {
        Game.getObstacles().clear();
        Game.getObstacles().add(new RectangleObstacle(new Vector2D(0d, 300d), new Vector2D(600d, 310d)));
        Player player = new Player();
        player.setPosition(new Vector2D(300d, 305d));
        boolean b = player.hasHitObstacle();
        assertTrue(b);
        player.setPosition(new Vector2D(300d, 320d));
        b = player.hasHitObstacle();
        assertFalse(b);
    }

    @Test
    public void hasHitObstacleFromUpdate() {
        Game.getObstacles().clear();
        Game.getObstacles().add(new RectangleObstacle(new Vector2D(0d, 300d), new Vector2D(600d, 310d)));
        Player player = new Player();
        player.setPosition(new Vector2D(400d, 305d));
        player.update();
//        assertTrue(player.dead);
    }

    @Test
    public void hasReachedGoalFromUpdate() {
        Player player = new Player();
        player.setPosition(Game.Setup.goal);
        player.update();
        assertTrue(player.hasReachedGoal());
    }
}