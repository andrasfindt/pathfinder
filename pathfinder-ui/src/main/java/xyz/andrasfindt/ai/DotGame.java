package xyz.andrasfindt.ai;

import xyz.andrasfindt.ai.internal.Population;

import java.util.HashMap;
import java.util.Map;

public class DotGame {
    public static final String DEFAULT_POPULATION = "default";
    private Map<String, Population> activePopulations = new HashMap<>();

    public DotGame(Listener listener) {
        this.activePopulations.put(DEFAULT_POPULATION, new Population(Game.Setup.POPULATION_SIZE, listener, 1000));
    }

    public void churn() {
        for (Population population : activePopulations.values()) {
            if (population.allCreepsDead()) {
                population.calculateFitness();
                population.naturalSelection();
                population.mutateChildren();
            } else {
                population.update();
                population.show();
            }
        }
    }

    public void increaseSpeed() {
        Game.Setup.SPEED_LIMIT += 0.01;
        if (Game.Setup.SPEED_LIMIT > 5) {
            Game.Setup.SPEED_LIMIT = 5;
        }
    }

    public void decreaseSpeed() {
        Game.Setup.SPEED_LIMIT -= 0.01;
        if (Game.Setup.SPEED_LIMIT < 0) {
            Game.Setup.SPEED_LIMIT = 0;
        }
    }

    public int getCurrentGeneration(String key) {
        return activePopulations.get(key).getCurrentGeneration();
    }

    public long getAliveCount(String key) {
        return activePopulations.get(key).getAliveCount();
    }
}
