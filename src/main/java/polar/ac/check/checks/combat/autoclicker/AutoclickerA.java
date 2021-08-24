package polar.ac.check.checks.combat.autoclicker;

import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.ArmAnimationEvent;
import polar.ac.event.client.FlyingEvent;
import polar.ac.utils.MathUtils;
import polar.ac.utils.PolarQueue;

import java.util.ArrayList;
import java.util.List;

public class AutoclickerA extends Check {

    private PolarQueue samples = new PolarQueue<Integer>(120);
    private List<Double> pastDiffs = new ArrayList<>();

    private int ticks;

    /**
     * Checking for mistake in clicker randomization
     *
     */


    public AutoclickerA(PlayerData data) {
        super(data, "Autoclicker", "A", "combat.autoclicker.a", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {
            if(data.getInteractionData().isDigging()) return;

            if (samples.size() >= 120) {

                final double kurtosis = MathUtils.getKurtosis(samples);
                final double stdDeviation = MathUtils.getStandardDeviation(samples);

                final double diff = Math.abs(kurtosis - stdDeviation);


                debug("diff=" + diff);


                if(pastDiffs.size() > 6) {

                    if (pastDiffs.contains(diff)) {
                        if (++buffer > 1.1D) {
                            fail("diff=" + diff);
                        }
                    } else if (buffer > 0) buffer -= 0.025D;


                }

                pastDiffs.add(diff);


            }


            if (ticks < 5)
                samples.add(ticks);


                this.ticks = 0;

        } else if (e instanceof FlyingEvent) {
            this.ticks++;
        }
    }
}
