package xyz.andrasfindt.ai;

import xyz.andrasfindt.ai.internals.Player;
import xyz.andrasfindt.ai.internals.Population;

public class Status {
    private boolean solved;
    private int stepsTaken;
    private int currentGeneration;
    private int aliveCount;
    private double speed;
    private double maxFitness;
    private double mutationRate;

    Status(Population population) {
        Player bestPlayer = population.getPreviousGenerationBestPlayer();
        solved = bestPlayer.hasReachedGoal();
        stepsTaken = bestPlayer.getStepCount();
        currentGeneration = population.getCurrentGeneration();
        aliveCount = population.getAliveCount();
        speed = Game.SPEED_LIMIT;
        maxFitness = bestPlayer.getFitness();
        mutationRate = Game.MUTATION_RATE;
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
