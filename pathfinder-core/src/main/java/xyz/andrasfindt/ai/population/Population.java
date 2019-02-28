package xyz.andrasfindt.ai.population;

import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.Listener;
import xyz.andrasfindt.ai.RandomUtil;
import xyz.andrasfindt.ai.Status;
import xyz.andrasfindt.ai.creep.BaseCreep;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Population {

    private final int genomeSize;
    protected BaseCreep[] creeps;
    protected int gen = 1;
    protected int bestCreep = 0;
    private Listener listener;
    private double fitnessSum = 0d;
    private int minStep;
    private BaseCreep oldBestCreep;

    public Population(Listener listener, BaseCreep[] creeps) {
        this.listener = listener;
        this.creeps = creeps;
        RandomUtil.setRandomSeed(Game.Setup.RANDOM_SEED);
        if (creeps.length == 0) {
            genomeSize = 0;
        } else {
            genomeSize = creeps[0].genomeSize;
        }
        this.minStep = genomeSize;
    }

    public int getGenomeSize() {
        return genomeSize;
    }

    public void show() {
        IntStream.iterate(creeps.length - 1, i -> i >= 0, i -> i - 1).forEachOrdered(i -> creeps[i].draw(listener));
    }

    public int getAliveCount() {
        long count = 0L;
        for (BaseCreep s : creeps) {
            if (!s.isDead()) {
                count++;
            }
        }
        return (int) count;
    }

    public void update() {
        for (BaseCreep creep : creeps) {//note:
            // for second stage optimization. (if we disable this, we don't die early, and keep pathfinding until brain runs out)
            // while it will never improve on the fitness of the overall population, it ensures that if the goal changes,
            // the population can be made to automatically adjust and use any dormant genes since last found path solution
            if (Game.Setup.TRUNCATE_POPULATION && creep.getGenome().step > minStep) {
                creep.setDead(true);
            } else {
                creep.update();
            }
        }
    }

    public void calculateFitness() {
        for (BaseCreep creep : creeps) {
            creep.calculateFitness();
        }
    }

    public boolean allCreepsDead() {
        return Arrays.stream(creeps).noneMatch(creep -> !creep.isDead() && !creep.hasReachedGoal());
    }

    public void naturalSelection() {
        BaseCreep[] newCreeps = new BaseCreep[creeps.length];//next gen
        setBestCreep();
        calculateFitnessSum();
        newCreeps[0] = creeps[bestCreep].makeChild();
        newCreeps[0].setBest(true);
        for (int i = 1; i < newCreeps.length; i++) {
            BaseCreep parent = selectParent();
            newCreeps[i] = parent.makeChild();
        }
        creeps = newCreeps.clone();
        gen++;
    }

    protected void calculateFitnessSum() {
        this.fitnessSum = Arrays.stream(creeps).map(BaseCreep::getFitness).reduce(0d, Double::sum);
    }

    protected BaseCreep selectParent() {
        double rand = RandomUtil.nextDouble(fitnessSum);
        double runningSum = 0d;
        for (BaseCreep creep : creeps) {
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

    protected void setBestCreep() {
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


    public BaseCreep getPreviousGenerationBestCreep() {
        return oldBestCreep;
    }

    public BaseCreep[] getCreeps() {
        return creeps;
    }

    public double getFitnessSum() {
        return fitnessSum;
    }

    public int getCurrentGeneration() {
        return gen;
    }
}
