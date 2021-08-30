package arctic.ac.check.checks.combat.autoclicker;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.ArmAnimationEvent;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.utils.ArcticQueue;
import arctic.ac.utils.EvictingList;
import arctic.ac.utils.MathUtils;

public class AutoclickerA extends Check {

    private ArcticQueue samples = new ArcticQueue<Integer>(120);
    private EvictingList<Double> pastDiffs = new EvictingList<>(15);

    private int ticks;

    /**
     * Checking for mistake in clicker randomization
     */


    public AutoclickerA(PlayerData data) {
        super(data, "Autoclicker", "A", "combat.autoclicker.a", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {
            if (data.getInteractionData().isDigging()) return;

            if (samples.size() >= 120) {

                final double kurtosis = MathUtils.getKurtosis(samples);
                final double stdDeviation = MathUtils.getStandardDeviation(samples);

                final double diff = Math.abs(kurtosis - stdDeviation);


                debug("diff=" + diff);


                if (pastDiffs.size() > 6) {

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
