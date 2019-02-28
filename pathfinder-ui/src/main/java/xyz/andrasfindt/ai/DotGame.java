package xyz.andrasfindt.ai;

import xyz.andrasfindt.ai.creep.BaseCreep;
import xyz.andrasfindt.ai.creep.BasicCreep;
import xyz.andrasfindt.ai.population.GamePopulation;
import xyz.andrasfindt.ai.population.Population;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class DotGame {
    public static final String DEFAULT_POPULATION = "default";
    private Map<String, Population> activePopulations = new HashMap<>();

    public DotGame(Listener listener) {
        BaseCreep[] creeps = IntStream.range(0, Game.Setup.POPULATION_SIZE).mapToObj(i -> new BasicCreep(Game.Setup.GENOME_SIZE)).toArray(BaseCreep[]::new);
        this.activePopulations.put(DEFAULT_POPULATION, new GamePopulation(listener, creeps));
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
