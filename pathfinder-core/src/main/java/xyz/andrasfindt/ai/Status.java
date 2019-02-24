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
    private int populationCount;
    private boolean truncatePopulation;
    private long randomSeed;
    private long stepsMax;
    private double disanceFromGoal;

    public Status(Population population) {
        Creep bestCreep = population.getPreviousGenerationBestCreep();
        disanceFromGoal = bestCreep.getPosition().distance(Game.Setup.goal);
        solved = bestCreep.hasReachedGoal();
        stepsTaken = bestCreep.getStepCount();
        currentGeneration = population.getCurrentGeneration();
        aliveCount = population.getAliveCount();
        speed = Game.Setup.SPEED_LIMIT;
        maxFitness = bestCreep.getFitness();
        mutationRate = Game.Setup.MUTATION_RATE;
        populationCount = Game.Setup.POPULATION_SIZE;
        truncatePopulation = Game.Setup.TRUNCATE_POPULATION;
        randomSeed = Game.Setup.RANDOM_SEED;
        stepsMax = population.getGenomeSize();
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

    public int getPopulationCount() {
        return populationCount;
    }

    public boolean getTruncatePopulation() {
        return truncatePopulation;
    }

    public long getRandomSeed() {
        return randomSeed;
    }

    public long getStepsMax() {
        return stepsMax;
    }

    public double getDisanceFromGoal() {
        return disanceFromGoal;
    }
}
