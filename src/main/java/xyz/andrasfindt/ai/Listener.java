package xyz.andrasfindt.ai;


import xyz.andrasfindt.ai.geom.Vector2D;

public interface Listener {
    void draw(Vector2D position, boolean isBest);

    void reset();

    void updateStats(Status status);
}
