package arctic.ac.check.checks.combat.autoclicker;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.ArmAnimationEvent;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.utils.ArcticQueue;
import arctic.ac.utils.MathUtils;
import arctic.ac.utils.Pair;

import java.util.List;

public class AutoclickerD extends Check {

    private int ticks;
    private double lastDifference;
    private ArcticQueue<Integer> samples = new ArcticQueue(20);

    public AutoclickerD(final PlayerData data) {
        super(data, "Autoclicker", "D", "combat.autoclicker.d", false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {

            if (this.samples.size() >= 20) {

                final Pair<List<Double>, List<Double>> outliers = MathUtils.getOutliers(samples);
                final int total = outliers.getX().size() + outliers.getY().size();

                final double deviation = MathUtils.getStandardDeviation(samples);
                final double difference = deviation - total;

                final double lastDifference = this.lastDifference;
                this.lastDifference = difference;

                final double result = Math.abs(difference - lastDifference);

                debug("dev=" + deviation + " outliers=" + total + " diff=" + difference + " result=" + result);

                if (result < 2.257D) {
                    buffer += (2.5 - result);
                    if (buffer > 3)
                        fail("result=" + result + " outliers=" + outliers + " deviation=" + deviation);
                } else if (buffer > 0) buffer -= 0.25D;
            }


            samples.add(this.ticks);
            this.ticks = 0;

        } else if (e instanceof FlyingEvent) {
            this.ticks++;
        }
    }
}
