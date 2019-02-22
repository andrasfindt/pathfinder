package xyz.andrasfindt.ai;

import xyz.andrasfindt.ai.internal.Population;

public class DotGame {
    private Population population;

    private Listener listener;

    public DotGame(Listener listener) {
        this.population = new Population(Game.Setup.POPULATION_SIZE, listener);
        this.listener = listener;
    }

    public void churn() {
        if (population.allDotsDead()) {
            population.calculateFitness();
            population.naturalSelection();
            population.mutateChildren();
            listener.updateStats(new Status(population));
        } else {
            population.update();
            population.show();
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

    public int getCurrentGeneration() {
        return population.getCurrentGeneration();
    }

    public long getAliveCount() {
        return population.getAliveCount();
    }
}
