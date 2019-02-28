package xyz.andrasfindt.ai.creep;

import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.RandomUtil;
import xyz.andrasfindt.ai.geom.Vector2D;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;

public class Genome {
    private static final double PI_2 = PI * 2d;
    public int step;
    Vector2D[] genes;
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
        List<Vector2D> list = new ArrayList<>();
        for (Vector2D direction : directions) {
            Vector2D copy = direction.copy();
            list.add(copy);
        }
        return list.toArray(new Vector2D[0]);
    }

    private void randomize() {
        int bound = genomeSize;
        for (int i = 0; i < bound; i++) {
            assignRandomDirection(i);
        }
    }

    Genome copy() {
        return new Genome(genes);
    }

    public void mutate() {
        mutate(0, genes.length);
    }

    public void mutate(int offset) {
        mutate(offset, genes.length - offset);
    }

    private void mutate(int offset, int size) {
        int bound = offset + size;
        if (bound > genes.length) {
            bound = genes.length;
        }
        for (int i = offset; i < bound; i++) {
            if (RandomUtil.nextDouble() < Game.Setup.MUTATION_RATE) {
                assignRandomDirection(i);
            }
        }
    }

    private void assignRandomDirection(int i) {
        genes[i] = Vector2D.fromAngle(RandomUtil.nextDouble(PI_2));
    }
}
