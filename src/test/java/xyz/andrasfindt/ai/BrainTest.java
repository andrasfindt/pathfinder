package xyz.andrasfindt.ai;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class BrainTest {

    @Before
    public void setUp() throws Exception {
        Game.RANDOM_SEED = 0;
        RandomUtil.setRandomSeed(0);
    }

    @Test
    public void testBrain() {
        Brain brain = new Brain(10);
        Arrays.stream(brain.directions).forEach(System.out::println);
    }
}