package honeybadger.ac.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtils {

    public static int floor(final double var0) {
        final int var2 = (int) var0;
        return var0 < var2 ? var2 - 1 : var2;
    }

    public double hypot(double x, double z) {
        return Math.sqrt((x * x) + (z * z));
    }

    /*
    GladUrBad GCD & Sensitivity
    Medusa (by GladUrBad) - https://github.com/GladUrBad/Medusa/blob/f00848c2576e4812283e6dc2dc05e29e2ced866a/Impl/src/main/java/com/gladurbad/medusa/util/MathUtil.java
    Spigot Post - https://www.spigotmc.org/threads/determining-a-players-sensitivity.468373/
     */

    public double getGcd(final double a, final double b) {
        if (a < b) {
            return getGcd(b, a);
        }

        if (Math.abs(b) < 0.001) {
            return a;
        } else {
            return getGcd(b, a - Math.floor(a / b) * b);
        }
    }

    public double getSensitivity(final float tDeltaPitch, final float tLastDeltaPitch) {
        final float deltaPitch = Math.abs(tDeltaPitch);
        final float lastDeltaPitch = Math.abs(tLastDeltaPitch);

        final float gcd = (float) MathUtils.getGcd(deltaPitch, lastDeltaPitch);
        final double sensMod = Math.cbrt(0.8333 * gcd);
        final double sensStepTwo = (1.666 * sensMod) - 0.3333;
        final double finalSens = sensStepTwo * 200;

        return finalSens;
    }
}