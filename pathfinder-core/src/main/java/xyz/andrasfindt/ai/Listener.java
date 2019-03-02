package xyz.andrasfindt.ai;


import xyz.andrasfindt.ai.creep.BaseCreep;

import javax.validation.constraints.NotNull;

public interface Listener {
    void draw(BaseCreep.ViewModel creep);

    void reset();

    void updateStats(@NotNull Status status);
}
