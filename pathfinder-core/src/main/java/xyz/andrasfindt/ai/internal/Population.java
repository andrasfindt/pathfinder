package xyz.andrasfindt.ai.internal;

import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.Listener;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Population {

    private Listener listener;

    private Player[] players;

    private double fitnessSum = 0d;

    private int gen = 1;

    private int bestPlayer = 0;

    private int minStep = 1000;

    private Player oldBestPlayer;

    public Population(int size, Listener listener) {
        RandomUtil.setRandomSeed(Game.Setup.RANDOM_SEED);
        this.listener = listener;
        players = new Player[size];
        for (int i = 0; i < size; i++) {
//            players[i] = new Player(ObstacleStrategy.BOUNCE);
            players[i] = new Player();
//            if (gen == 1) {
//                player.setStrategy();
//            }
        }
    }

    public void show() {
        IntStream.iterate(players.length - 1, i -> i >= 0, i -> i - 1).forEachOrdered(i -> players[i].draw(listener));
    }

    public int getAliveCount() {
        return (int) Arrays.stream(players).filter(s -> !s.isDead()).count();
    }

    public void update() {
        Arrays.stream(players).forEach(player -> {
            //note: for second stage optimization. (if we disable this, we don't die early, and keep pathfinding until brain runs out)
            if (Game.Setup.TRUNCATE_POPULATION && player.getGenome().step > minStep) {
                player.setDead(true);
            } else {
                player.update();
            }
        });
    }

    public void calculateFitness() {
        for (Player player : players) {
            player.calculateFitness();
        }
    }

    public boolean allDotsDead() {
        return Arrays.stream(players).noneMatch(player -> !player.isDead() && !player.hasReachedGoal());
    }

    public void naturalSelection() {
        Player[] newPlayers = new Player[players.length];//next gen
        setBestDot();
        calculateFitnessSum();
        newPlayers[0] = players[bestPlayer].makeChild();
        newPlayers[0].setBest(true);
        for (int i = 1; i < newPlayers.length; i++) {
            Player parent = selectParent();
            newPlayers[i] = parent.makeChild();
        }
        players = newPlayers.clone();
        gen++;
    }

    void calculateFitnessSum() {
        this.fitnessSum = Arrays.stream(players).map(Player::getFitness).reduce(0d, Double::sum);
    }

    private Player selectParent() {
        double rand = RandomUtil.nextDouble(fitnessSum);
        double runningSum = 0d;
        for (Player player : players) {
            runningSum += player.getFitness();
            if (runningSum > rand) {
                return player;
            }
        }
        //should never get to this point
        return null;
    }

    public void mutateChildren() {
        IntStream.range(1, players.length).forEach(i -> players[i].getGenome().mutate());
    }

    private void setBestDot() {
        double max = 0d;
        int maxIndex = 0;
        for (int i = 0; i < players.length; i++) {
            if (players[i].getFitness() > max) {
                max = players[i].getFitness();
                maxIndex = i;
            }
        }
        bestPlayer = maxIndex;
        oldBestPlayer = players[bestPlayer];
        if (players[bestPlayer].hasReachedGoal()) {
            minStep = players[bestPlayer].getGenome().step;
        }
        System.out.printf("%s gen: %d max: %s step: %d max_speed: %f\n", players[bestPlayer].hasReachedGoal() ? "*" : " ", gen, max, players[bestPlayer].getGenome().step, Game.Setup.SPEED_LIMIT);
    }


    public Player getPreviousGenerationBestPlayer() {
        return oldBestPlayer;
    }

    public Player[] getPlayers() {
        return players;
    }

    public double getFitnessSum() {
        return fitnessSum;
    }

    public int getCurrentGeneration() {
        return gen;
    }
}
