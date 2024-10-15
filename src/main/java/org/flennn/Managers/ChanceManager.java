package org.flennn.Managers;

import java.util.Random;

public class ChanceManager {
    private static final Random random = new Random();

    public static boolean isChanceSuccessful(int baseChance, int level, int factor) {
        int totalChance = baseChance + (level * factor);
        return random.nextInt(1000) < totalChance;
    }


}
