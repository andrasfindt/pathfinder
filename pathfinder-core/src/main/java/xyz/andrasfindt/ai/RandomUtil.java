package xyz.andrasfindt.ai;

import java.util.Random;
import java.util.UUID;

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

    public static UUID nextUUID() {
        return UUID.randomUUID();
    }
}
