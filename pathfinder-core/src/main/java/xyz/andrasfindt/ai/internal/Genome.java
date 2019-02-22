package xyz.andrasfindt.ai.internal;

import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.geom.Vector2D;

import java.util.Arrays;
import java.util.stream.IntStream;

import static java.lang.Math.PI;

class Genome {
    private static final double PI_2 = PI * 2d;
    Vector2D[] genes;
    int step;
    int genomeSize;

    Genome(int genomeSize) {
        this.genes = new Vector2D[genomeSize];
        this.step = 0;
        this.genomeSize = genomeSize;
        randomize();
    }

    private Genome(Vector2D[] genes) {
        this.genes = copyDirections(genes);
        this.step = 0;
        this.genomeSize = genes.length;
    }

    private Vector2D[] copyDirections(Vector2D[] directions) {
        return Arrays.stream(directions).map(Vector2D::copy).toArray(Vector2D[]::new);
    }

    private void randomize() {
        IntStream.range(0, genomeSize).forEach(this::assignRandomDirection);
    }

    Genome copy() {
        return new Genome(genes);
    }

    void mutate() {
        IntStream.range(0, genes.length).filter(i -> RandomUtil.nextDouble() < Game.Setup.MUTATION_RATE).forEach(this::assignRandomDirection);
    }

    private void assignRandomDirection(int i) {
        genes[i] = Vector2D.fromAngle(RandomUtil.nextDouble(PI_2));
    }
}
