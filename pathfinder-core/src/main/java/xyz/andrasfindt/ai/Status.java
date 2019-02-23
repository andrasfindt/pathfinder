package xyz.andrasfindt.ai;

import xyz.andrasfindt.ai.internal.Creep;
import xyz.andrasfindt.ai.internal.Population;

public class Status {
    private boolean solved;
    private int stepsTaken;
    private int currentGeneration;
    private int aliveCount;
    private double speed;
    private double maxFitness;
    private double mutationRate;

    public Status(Population population) {
        Creep bestCreep = population.getPreviousGenerationBestCreep();
        solved = bestCreep.hasReachedGoal();
        stepsTaken = bestCreep.getStepCount();
        currentGeneration = population.getCurrentGeneration();
        aliveCount = population.getAliveCount();
        speed = Game.Setup.SPEED_LIMIT;
        maxFitness = bestCreep.getFitness();
        mutationRate = Game.Setup.MUTATION_RATE;
    }

    public boolean isSolved() {
        return solved;
    }

    public int getStepsTaken() {
        return stepsTaken;
    }

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    public int getAliveCount() {
        return aliveCount;
    }

    public double getSpeed() {
        return speed;
    }

    public double getMaxFitness() {
        return maxFitness;
    }

    public double getMutationRate() {
        return mutationRate;
    }
}
