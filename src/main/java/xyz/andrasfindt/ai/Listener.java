package xyz.andrasfindt.ai;


public interface Listener {
    void draw(Vector2D position, boolean isBest);

    void reset();

    void updateStats(Player bestPlayer);
}
