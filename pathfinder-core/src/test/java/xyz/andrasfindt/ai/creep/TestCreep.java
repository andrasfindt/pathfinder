package xyz.andrasfindt.ai.creep;

public class TestCreep extends BaseCreep {
    public TestCreep(int genomeSize) {
        super(genomeSize);
    }

    @Override
    protected ViewModel makeViewModel() {
        return null;
    }

    @Override
    protected double getSpeedLimit() {
        return 5;
    }

    @Override
    protected BaseCreep makeNew() {
        return new TestCreep(genomeSize);
    }
}
