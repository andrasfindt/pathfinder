package xyz.andrasfindt.ai.internal;

import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.Listener;
import xyz.andrasfindt.ai.Status;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Population {

    private final int genomeSize;
    private Listener listener;
    private Creep[] creeps;
    private double fitnessSum = 0d;
    private int gen = 1;
    private int bestCreep = 0;
    private int minStep;
    private Creep oldBestCreep;

    public Population(int size, Listener listener, int genomeSize) {
        this.genomeSize = genomeSize;
        this.minStep = genomeSize;
        RandomUtil.setRandomSeed(Game.Setup.RANDOM_SEED);
        this.listener = listener;
        creeps = new Creep[size];
        for (int i = 0; i < size; i++) {
//            creeps[i] = new Creep(ObstacleStrategy.BOUNCE);
            creeps[i] = new Creep(this.genomeSize);
//            if (gen == 1) {
//                player.setStrategy();
//            }
        }
    }

    public int getGenomeSize() {
        return genomeSize;
    }

    public void show() {
        IntStream.iterate(creeps.length - 1, i -> i >= 0, i -> i - 1).forEachOrdered(i -> creeps[i].draw(listener));
    }

    public int getAliveCount() {
        return (int) Arrays.stream(creeps).filter(s -> !s.isDead()).count();
    }

    public void update() {
        Arrays.stream(creeps).forEach(creep -> {
            //note:
            // for second stage optimization. (if we disable this, we don't die early, and keep pathfinding until brain runs out)
            // while it will never improve on the fitness of the overall population, it ensures that if the goal changes,
            // the population can be made to automatically adjust and use any dormant genes since last found path solution
            if (Game.Setup.TRUNCATE_POPULATION && creep.getGenome().step > minStep) {
                creep.setDead(true);
            } else {
                creep.update();
            }
        });
    }

    public void calculateFitness() {
        for (Creep creep : creeps) {
            creep.calculateFitness();
        }
    }

    public boolean allCreepsDead() {
        return Arrays.stream(creeps).noneMatch(creep -> !creep.isDead() && !creep.hasReachedGoal());
    }

    public void naturalSelection() {
        Creep[] newCreeps = new Creep[creeps.length];//next gen
        setBestCreep();
        calculateFitnessSum();
        newCreeps[0] = creeps[bestCreep].makeChild();
        newCreeps[0].setBest(true);
        for (int i = 1; i < newCreeps.length; i++) {
            Creep parent = selectParent();
            newCreeps[i] = parent.makeChild();
        }
        creeps = newCreeps.clone();
        gen++;
    }

    void calculateFitnessSum() {
        this.fitnessSum = Arrays.stream(creeps).map(Creep::getFitness).reduce(0d, Double::sum);
    }

    private Creep selectParent() {
        double rand = RandomUtil.nextDouble(fitnessSum);
        double runningSum = 0d;
        for (Creep creep : creeps) {
            runningSum += creep.getFitness();
            if (runningSum > rand) {
                return creep;
            }
        }
        //should never get to this point
        return null;
    }

    public void mutateChildren() {
        IntStream.range(1, creeps.length).forEach(i -> creeps[i].getGenome().mutate());
    }

    private void setBestCreep() {
        double max = 0d;
        int maxIndex = 0;
        for (int i = 0; i < creeps.length; i++) {
            if (creeps[i].getFitness() > max) {
                max = creeps[i].getFitness();
                maxIndex = i;
            }
        }
        bestCreep = maxIndex;
        oldBestCreep = creeps[bestCreep];
        if (creeps[bestCreep].hasReachedGoal()) {
            minStep = creeps[bestCreep].getGenome().step;
        }
        listener.updateStats(new Status(this));
    }


    public Creep getPreviousGenerationBestCreep() {
        return oldBestCreep;
    }

    public Creep[] getCreeps() {
        return creeps;
    }

    public double getFitnessSum() {
        return fitnessSum;
    }

    public int getCurrentGeneration() {
        return gen;
    }
}
