package xyz.andrasfindt.ai.internals;

import java.util.Random;

public class RandomUtil {
    private static Random RANDOM = new Random(System.currentTimeMillis());

    public static void setRandomSeed(long seed) {
        RANDOM.setSeed(seed);
    }

    public static double nextDouble(double min, double max) {
        return (RANDOM.nextDouble() * (max - min)) + min;
    }

    public static double nextDouble(double max) {
        return RANDOM.nextDouble() * max;
    }

    public static double nextDouble() {
        return RANDOM.nextDouble();
    }
}
