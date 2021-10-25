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

public class AutoclickerF extends Check {

    private int ticks, lastOutliers;
    private double lastSkewness, lastSkewnessDiff;
    private ArcticQueue<Integer> samples = new ArcticQueue<>(10);


    public AutoclickerF(PlayerData data) {
        super(data, "Autoclicker", "F", "combat.autoclicker.f", "Checks for consistency.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {
            if (data.getInteractionData().isDigging()) return;

            if (this.samples.size() >= 10) {

                final Pair<List<Double>, List<Double>> outliersPair = MathUtils.getOutliers(samples);

                final int outliers = outliersPair.getX().size() + outliersPair.getY().size();
                final double skewness = Math.abs(MathUtils.getSkewness(samples));

                final int lastOutliers = this.lastOutliers;
                this.lastOutliers = outliers;

                final double lastSkewness = this.lastSkewness;
                this.lastSkewness = skewness;

                final int outlierDifference = Math.abs(outliers - lastOutliers);
                final double skewnessDifference = Math.abs(skewness - lastSkewness);


                debug("outliersDiff=" + outlierDifference + " skewnessDiff=" + skewnessDifference);

                if((skewnessDifference < 1.0E-5 || (Double.isNaN(skewnessDifference) && Double.isNaN(lastSkewnessDiff))) && outlierDifference <= 1) {
                    if(++buffer > 3)
                        fail("outliersDiff=" + outlierDifference + " skewness=" + skewness + " skewDiff=" + skewnessDifference);

                } else if(buffer > 0) buffer -= 0.25D;

                this.lastSkewnessDiff = skewnessDifference;
            }
            samples.add(this.ticks);
            this.ticks = 0;
        } else if (e instanceof FlyingEvent) {
            this.ticks++;
        }
    }
}
