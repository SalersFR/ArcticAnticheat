package arctic.ac.utils;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class MathUtils {

    public final double EXPANDER = Math.pow(2, 24);


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

    public double gcd(final double limit, final double a, final double b) {
        return b <= limit ? a : MathUtils.gcd(limit, b, a % b);
    }


    // Taken from https://github.com/ElevatedDev/Frequency

    /**
     * @param current  - The current value
     * @param previous - The previous value
     * @return - The GCD of those two values
     */
    public long getGcd(final long current, final long previous) {
        return (previous <= 16384L) ? current : getGcd(previous, current % previous);
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


    public double getStandardDeviation(final Collection<? extends Number> data) {
        final double variance = getVariance(data);

        // The standard deviation is the square root of variance. (sqrt(s^2))
        return Math.sqrt(variance);
    }

    public double getVariance(final Collection<? extends Number> data) {
        int count = 0;

        double sum = 0.0;
        double variance = 0.0;

        final double average;

        // Increase the sum and the count to find the average and the standard deviation
        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;
        }

        average = sum / count;

        // Run the standard deviation formula
        for (final Number number : data) {
            variance += Math.pow(number.doubleValue() - average, 2.0);
        }

        return variance;
    }

    public double getKurtosis(final Collection<? extends Number> data) {
        double sum = 0.0;
        int count = 0;

        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;
        }

        if (count < 3.0) {
            return 0.0;
        }

        final double efficiencyFirst = count * (count + 1.0) / ((count - 1.0) * (count - 2.0) * (count - 3.0));
        final double efficiencySecond = 3.0 * Math.pow(count - 1.0, 2.0) / ((count - 2.0) * (count - 3.0));
        final double average = sum / count;

        double variance = 0.0;
        double varianceSquared = 0.0;

        for (final Number number : data) {
            variance += Math.pow(average - number.doubleValue(), 2.0);
            varianceSquared += Math.pow(average - number.doubleValue(), 4.0);
        }

        return efficiencyFirst * (varianceSquared / Math.pow(variance / sum, 2.0)) - efficiencySecond;
    }

    public int getRecurring(List<Long> list, long query) {
        int total = 0;

        for (long l : list) {
            if (l == query) total += 1;
        }

        return total;
    }

    public boolean recurringPattern(List<Long> list, int lowerThanForPattern, int maxPatternCount) {
        int total = 0;

        for (long l : list) {
            if (l < lowerThanForPattern) {
                total += 1;
            }
        }

        if (total > maxPatternCount) {
            return true;
        }
        return false;
    }

    public double getSkewness(final Collection<? extends Number> data) {
        double sum = 0;
        int count = 0;

        final List<Double> numbers = Lists.newArrayList();

        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;

            numbers.add(number.doubleValue());
        }

        Collections.sort(numbers);

        final double mean = sum / count;
        final double median = (count % 2 != 0) ? numbers.get(count / 2) : (numbers.get((count - 1) / 2) + numbers.get(count / 2)) / 2;
        final double variance = getVariance(data);

        return 3 * (mean - median) / variance;
    }
}