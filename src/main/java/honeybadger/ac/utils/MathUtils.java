package honeybadger.ac.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtils {

    public static int floor(final double var0) {
        final int var2 = (int) var0;
        return var0 < var2 ? var2 - 1 : var2;
    }

    public double hypot(double x,double z) {
        return Math.sqrt((x*x) + (z*z));
    }
}