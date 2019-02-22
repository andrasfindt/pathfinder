package xyz.andrasfindt.ai.obstacle;

import xyz.andrasfindt.ai.geom.Vector2D;

public abstract class Obstacle {
    public abstract boolean hit(Vector2D position);
}
