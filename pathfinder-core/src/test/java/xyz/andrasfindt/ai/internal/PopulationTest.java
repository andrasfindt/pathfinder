package xyz.andrasfindt.ai.internal;

import org.junit.Test;
import xyz.andrasfindt.ai.Listener;
import xyz.andrasfindt.ai.Status;
import xyz.andrasfindt.ai.geom.Vector2D;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PopulationTest {

    @Test
    public void calculateFitnessSum() {//todo mocks.
        BaseCreep[] creeps = new BaseCreep[2];
        creeps[0] = new TestCreep(100);
        creeps[1] = new TestCreep(100);

        Population population = new Population(new Listener() {
            @Override
            public void draw(Vector2D position, boolean isBest) {
                System.out.printf("drawing %s as %s\n", position, isBest ? "best" : "normal dot");
            }

            @Override
            public void reset() {
                System.out.println("reset");
            }

            @Override
            public void updateStats(Status BestDot) {

            }
        }, creeps);
        Arrays.stream(population.getCreeps()).forEach(creep -> creep.setFitness(1d));
        population.calculateFitnessSum();
        assertEquals(2.0, population.getFitnessSum(), 0.00005);
    }
}