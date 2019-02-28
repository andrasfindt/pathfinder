package xyz.andrasfindt.ai.creep;

public class TestCreep extends BaseCreep {
    public TestCreep(int genomeSize) {
        super(genomeSize);
    }

    @Override
    protected BaseCreep makeNew() {
        return new TestCreep(genomeSize);
    }
}
