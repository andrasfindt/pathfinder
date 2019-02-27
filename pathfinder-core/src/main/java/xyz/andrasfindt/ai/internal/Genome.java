package xyz.andrasfindt.ai.internal;

import xyz.andrasfindt.ai.Game;
import xyz.andrasfindt.ai.geom.Vector2D;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;

public class Genome {
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

    void mutate() {
        int bound = genes.length;
        for (int i = 0; i < bound; i++) {
            if (RandomUtil.nextDouble() < Game.Setup.MUTATION_RATE) {
                assignRandomDirection(i);
            }
        }
    }

    private void assignRandomDirection(int i) {
        genes[i] = Vector2D.fromAngle(RandomUtil.nextDouble(PI_2));
    }
}
