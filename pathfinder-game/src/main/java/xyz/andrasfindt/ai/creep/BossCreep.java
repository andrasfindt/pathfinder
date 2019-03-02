package xyz.andrasfindt.ai.creep;

import xyz.andrasfindt.ai.Game;

public class BossCreep extends BasicCreep implements Boss {
    private static final int DEFAULT_HEALTH = 5;

    public BossCreep(int genomeSize) {
        super(genomeSize, DEFAULT_HEALTH);
    }

    public BossCreep(Genome genome) {
        super(genome);
        genome.mutate(genomeSize / 2);
        this.health = DEFAULT_HEALTH;
    }

    protected BossCreep(int genomeSize, int health) {
        super(genomeSize, health);
    }

    @Override
    protected double getSpeedLimit() {
        return Game.Setup.SPEED_LIMIT / 1.03125d;
    }

    //todo
    // think about implementing boss children
/*
    @Override
    protected BaseCreep makeNew() {
        return new BossCreep(genomeSize, health);
    }
*/

    @Override
    protected BaseCreep.ViewModel makeViewModel() {
        return new ViewModel(this);
    }

    public class ViewModel extends BasicCreep.ViewModel {

        ViewModel(BossCreep creep) {
            super(creep);
            setRadius(3d);
            setPaint(new Paint(1.0d, 0.0d, 1.0d));
        }
    }
}
