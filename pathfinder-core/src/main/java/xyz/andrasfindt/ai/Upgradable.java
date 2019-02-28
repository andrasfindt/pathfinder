package xyz.andrasfindt.ai;

import xyz.andrasfindt.ai.creep.BaseCreep;

import java.lang.reflect.InvocationTargetException;

public interface Upgradable {
    <T extends BaseCreep> T upgrade(Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;
}
