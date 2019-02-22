package xyz.andrasfindt.ai.internal;

import org.junit.Test;
import xyz.andrasfindt.ai.Listener;
import xyz.andrasfindt.ai.Status;
import xyz.andrasfindt.ai.geom.Vector2D;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PopulationTest {

    @Test
    public void calculateFitnessSum() {
        Population population = new Population(2, new Listener() {
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
        });
        Arrays.stream(population.getPlayers()).forEach(dot -> dot.setFitness(1d));
        population.calculateFitnessSum();
        assertEquals(2.0, population.getFitnessSum(), 0.00005);
    }
}