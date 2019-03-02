package xyz.andrasfindt.ai.creep;

import xyz.andrasfindt.ai.Upgradable;
import xyz.andrasfindt.ai.obstacle.Obstacle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BasicCreep extends DestroyableCreep implements Upgradable {

    private static final int DEFAULT_HEALTH = 1;

    public BasicCreep(int genomeSize) {
        super(genomeSize, DEFAULT_HEALTH);
    }

    @Override
    protected BaseCreep.ViewModel makeViewModel() {
        return new ViewModel(this);
    }

    protected BasicCreep(Genome genome) {
        super(genome.genomeSize, DEFAULT_HEALTH);
        this.genome = genome.copy();
    }

    protected BasicCreep(int genomeSize, int health) {
        super(genomeSize, health);
    }

    @Override
    protected BaseCreep makeNew() {
        return new BasicCreep(genomeSize);
    }

    @Override
    protected void takeHit(Obstacle obstacle) {
        if (--health <= 0) {
            dead = true;
        }
    }

    @Override
    public BaseCreep makeChild() {
        return super.makeChild();
    }

    @Override
    public <T extends BaseCreep> T upgrade(Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<T> constructor = clazz.getDeclaredConstructor(Genome.class);
        T creep = constructor.newInstance(genome);
        return creep;
    }

    protected class ViewModel extends DestroyableCreep.ViewModel<BasicCreep> {
        ViewModel(BasicCreep creep) {
            super(creep);
        }
    }
}
