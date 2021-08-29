package arctic.ac.check.checks.combat.autoclicker;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.ArmAnimationEvent;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.utils.ArcticQueue;
import arctic.ac.utils.MathUtils;

public class AutoclickerB extends Check {

    private ArcticQueue samples = new ArcticQueue<Integer>(120);


    private int ticks;
    private double lastDiff;

    public AutoclickerB(PlayerData data) {
        super(data, "Autoclicker", "B", "combat.autoclicker.b", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {
            if (data.getInteractionData().isDigging()) return;

            final double kurtosis = MathUtils.getKurtosis(samples);
            final double stdDeviation = MathUtils.getStandardDeviation(samples);

            if (samples.size() >= 120) {
                final double diff = Math.abs(kurtosis - stdDeviation);
                final double result = Math.abs(diff - lastDiff);
                debug("lastDiff=" + lastDiff + " diff=" + diff + " result=" + result);

                if (result < 2.5D) {
                    if (buffer < 8) buffer += (2.5 - result);

                    if (buffer > 3) {
                        fail("result=" + result);
                    }
                } else if (buffer > 0) buffer--;

                this.lastDiff = diff;

            }


            if (ticks < 8)
                samples.add(ticks);


            this.ticks = 0;


        } else if (e instanceof FlyingEvent) {
            this.ticks++;
        }
    }
}