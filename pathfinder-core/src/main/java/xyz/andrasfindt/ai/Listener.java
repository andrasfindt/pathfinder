package xyz.andrasfindt.ai;


import xyz.andrasfindt.ai.creep.BaseCreep;
import xyz.andrasfindt.ai.geom.Vector2D;

import javax.validation.constraints.NotNull;

public interface Listener {
    void draw(@NotNull Vector2D position, boolean isBest);

    void draw(@NotNull BaseCreep creep);

    void reset();

    void updateStats(@NotNull Status status);
}
