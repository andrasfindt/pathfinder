package xyz.andrasfindt.ai.creep;

public class BossCreep extends BasicCreep implements Boss {
    private static final int DEFAULT_HEALTH = 5;

    public BossCreep(int genomeSize) {
        super(genomeSize, DEFAULT_HEALTH);
    }

    public BossCreep(Genome genome) {
        super(genome);
        this.health = DEFAULT_HEALTH;
    }

    protected BossCreep(int genomeSize, int health) {
        super(genomeSize, health);
    }

    //todo
    // think about implementing boss children
/*
    @Override
    protected BaseCreep makeNew() {
        return new BossCreep(genomeSize, health);
    }
*/
}
