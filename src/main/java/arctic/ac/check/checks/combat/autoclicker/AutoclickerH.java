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

public class AutoclickerH extends Check {

    private final ArcticQueue<Integer> samples = new ArcticQueue(25);
    private int ticks;

    public AutoclickerH(PlayerData data) {
        super(data, "Autoclicker", "H", "combat.autoclicker.h", "Checks for impossible patterns", false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {


            if (samples.size() >= 25) {

                final Pair<List<Double>, List<Double>> outliersPair = MathUtils.getOutliers(samples);
                final int outliers = outliersPair.getX().size() + outliersPair.getY().size();

                final double variance = MathUtils.getVariance(samples);

                debug("outliers=" + outliers + " variance=" + variance);

                if(variance == outliers || variance < 2.25D && outliers <= 2) {
                    if(++buffer > 3)
                        fail("variance=" + variance + "outliers=" + outliers);
                } else if(buffer > 0) buffer -= 0.12D;


            }
            this.samples.add(this.ticks);
            this.ticks = 0;
        } else if (e instanceof FlyingEvent) {
            this.ticks++;

        }

    }
}
