package dev.arctic.anticheat.utilities;

import com.comphenix.protocol.wrappers.Pair;
import com.google.common.collect.Lists;
import dev.arctic.anticheat.utilities.mc.MathHelper;
import lombok.experimental.UtilityClass;
import net.jitse.npclib.utilities.MathUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public double getReversedModulus(float div, float a, double remainder) {
        if (a < remainder)
            return (remainder - a);

        return (div + remainder - a);
    }

    public double getMode(ArrayList<Double> list) {
        double maxNumber = -1;
        double maxAppearances = -1;

        for (int i = 0; i < list.size(); i++) {
            int count = 0;

            for (int j = 0; j < list.size(); j++) {
                if (list.get(i) == list.get(j))
                    count++;
            }

            if (count > maxAppearances) {
                maxNumber = list.get(i);
                maxAppearances = count;
            }
        }

        return maxNumber;
    }

    public static double getEntropy(EvictingList<Integer> values) {
        if (values.isEmpty()) {
            return -1.0;
        }//from  w  w  w  .  j  ava 2s  . c om

        double noOfTerms = 0;
        for (Number frequency : values) {
            noOfTerms += frequency.doubleValue();
        }

        double entropy = 0.0;
        for (Number frequency : values) {
            double currentProbability = frequency.doubleValue() / noOfTerms;
            entropy += -currentProbability * Math.log(currentProbability) / Math.log(2);
        }

        return entropy;
    }


    public boolean isScientificNotation(final Number num) {
        return num.doubleValue() < .001D;
    }
    /*
    GladUrBad GCD & Sensitivity
    Medusa (by GladUrBad) - https://github.com/GladUrBad/Medusa/blob/f00848c2576e4812283e6dc2dc05e29e2ced866a/Impl/src/main/java/com/gladurbad/medusa/util/MathUtil.java
    Spigot Post - https://www.spigotmc.org/threads/determining-a-players-sensitivity.468373/
     */

    public boolean areAllEqual(double... values) {
        if (values.length == 0) {
            return true; // Alternative below
        }
        double checkValue = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] != checkValue) {
                return false;
            }
        }
        return true;
    }
    /**
     *
     * @param data - The sample of clicks you want to get the cps from
     * @return - The cps using the average as a method of calculation
     */
    public double getCps(final Collection<? extends Number> data) {
        final double average = data.stream().mapToDouble(Number::doubleValue).average().orElse(0.0);

        return 20 / average;
    }



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

    public static long getAbsoluteGcd(final float current, final float last) {

        final long currentExpanded = (long) (current * EXPANDER);

        final long lastExpanded = (long) (last * EXPANDER);

        return getGcd(currentExpanded, lastExpanded);
    }


    public double gcd(final double limit, final double a, final double b) {
        return b <= limit ? a : MathUtils.gcd(limit, b, a % b);
    }

    public double gcdFromAList(ArrayList<Float> list, double currDelta) {
        //For each delta
        double var1 = currDelta;
        for (float num : list) {
            var1 = gcd(0x4000, num, var1);
        }

        return var1;
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

    public double averageFloat(ArrayList<Float> list) {
        double sum = 0;
        double size = list.size();

        for (double var1 : list) {
            sum += var1;
        }

        return sum / size;
    }

    public double averageLong(ArrayList<Long> list) {
        double sum = 0;
        double size = list.size();

        for (double var1 : list) {
            sum += var1;
        }

        return sum / size;
    }

    public double averageInt(EvictingList<Integer> list) {
        double sum = 0;
        double size = list.size();

        for (double var1 : list) {
            sum += var1;
        }

        return sum / size;
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

    public int getRecurring2(List<Integer> list, long query) {
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

    public boolean recurringPattern2(List<Integer> list, int lowerThanForPattern, int maxPatternCount) {
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

    /**
     * @param data - The data you want the median from
     * @return - The middle number of that data
     * @See - https://en.wikipedia.org/wiki/Median
     */
    private double getMedian(final List<Double> data) {
        if (data.size() % 2 == 0) {
            return (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2;
        } else {
            return data.get(data.size() / 2);
        }
    }


    /**
     * @param - The collection of numbers you want analyze
     * @return - A pair of the high and low outliers
     * @See - https://en.wikipedia.org/wiki/Outlier
     */
    public Pair<List<Double>, List<Double>> getOutliers(final Collection<? extends Number> collection) {
        final List<Double> values = new ArrayList<>();

        for (final Number number : collection) {
            values.add(number.doubleValue());
        }

        final double q1 = getMedian(values.subList(0, values.size() / 2));
        final double q3 = getMedian(values.subList(values.size() / 2, values.size()));

        final double iqr = Math.abs(q1 - q3);
        final double lowThreshold = q1 - 1.5 * iqr, highThreshold = q3 + 1.5 * iqr;

        final Pair<List<Double>, List<Double>> tuple = new Pair<>(new ArrayList<>(), new ArrayList<>());

        for (final Double value : values) {
            if (value < lowThreshold) {
                tuple.getFirst().add(value);
            } else if (value > highThreshold) {
                tuple.getSecond().add(value);
            }
        }

        return tuple;
    }

    public static int getDistinct(final Collection<? extends Number> collection) {
        Set<Object> set = new HashSet<>(collection);
        return set.size();
    }

    public static int getSames(final Collection<? extends Number> collection) {
        return collection.size() - getDistinct(collection);
    }


}
