package fancywords;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Random;

/**
 * Created by Shrec on 6/10/2017.
 */
public class Utils {
    public final static double MEMORY_DIV_K = 1000L * 60 * 60 * 24 * 30;
    static Random random = new Random(System.nanoTime());

    public static double getMastery(double logAbsoluteMastery, long time) {
        return Math.exp(logAbsoluteMastery - time / MEMORY_DIV_K);
    }

    public static double getLogAbsoluteMastery(double mastery, long time) {
        return Math.log(mastery) + time / MEMORY_DIV_K;
    }

    public static double increaseLogAbsoluteMasteryByOneByTime(double logAbsoluteMastery, long time) {
        return getLogAbsoluteMastery(getMastery(logAbsoluteMastery, time) + 1, time);
    }

    public static byte randomIn4() {
        return (byte) Math.abs(random.nextInt() % 4);
    }

    public static String toJSONArray(String[] array) {
        StringBuilder stringBuilder = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            stringBuilder.append("\"")
                    .append(array[i])
                    .append("\"");
            if (i < array.length - 1) stringBuilder.append(",");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public static String toPercentage(double value) {
        return Math.round(100 * value) + "%";
    }

    public static String escapeEcmaScript(String input) {
        return StringEscapeUtils.escapeEcmaScript(input);
    }
}
