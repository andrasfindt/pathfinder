package xyz.andrasfindt.ai.internals;

import org.junit.Before;
import org.junit.Test;
import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.internals.Genome;
import xyz.andrasfindt.ai.internals.RandomUtil;

import java.util.Arrays;

public class GenomeTest {

    @Before
    public void setUp() throws Exception {
        Game.RANDOM_SEED = 0;
        RandomUtil.setRandomSeed(0);
    }

    @Test
    public void testBrain() {
        Genome genome = new Genome(10);
        Arrays.stream(genome.genes).forEach(System.out::println);
    }
}