package org.ianShowTechniek.Util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ToolBox {

    public static double random(int max) {
        return random(0, max);
    }

    public static double random(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max + 1);
    }

}
