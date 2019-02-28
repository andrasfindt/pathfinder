package xyz.andrasfindt.ai.population;

import xyz.andrasfindt.ai.Listener;
import xyz.andrasfindt.ai.Upgradable;
import xyz.andrasfindt.ai.creep.BaseCreep;
import xyz.andrasfindt.ai.creep.BossCreep;

import java.lang.reflect.InvocationTargetException;

public class GamePopulation extends Population {
    public GamePopulation(Listener listener, BaseCreep[] creeps) {
        super(listener, creeps);
    }

    @Override
    public void naturalSelection() {


        gen++;
        BaseCreep[] newCreeps = new BaseCreep[creeps.length];//next gen
        setBestCreep();
        calculateFitnessSum();

        BaseCreep creep = creeps[bestCreep];
        if (gen % 10 == 0 && creep instanceof Upgradable) {
            try {
                newCreeps[0] = ((Upgradable) creep).upgrade(BossCreep.class);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
                newCreeps[0] = creep.makeChild();
            }
        } else {
            newCreeps[0] = creep.makeChild();
        }
        newCreeps[0].setBest(true);
        for (int i = 1; i < newCreeps.length; i++) {
            BaseCreep parent = selectParent();
            newCreeps[i] = parent.makeChild();
        }
        creeps = newCreeps.clone();

        /*
        super.naturalSelection();
        if (gen % 10 == 0) {
            if (creeps[0] instanceof BasicCreep) {
                try {
                    creeps[0] = ((Upgradable) creeps[0]).upgrade(BossCreep.class);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }
}
