package polar.ac.check.checks.combat.autoclicker;

import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.ArmAnimationEvent;
import polar.ac.event.client.FlyingEvent;
import polar.ac.utils.MathUtils;
import polar.ac.utils.PolarQueue;

public class AutoclickerC extends Check {

    private final PolarQueue samples = new PolarQueue<Integer>(120);
    private int ticks;

    public AutoclickerC(PlayerData data) {
        super(data, "Autoclicker", "C", "combat.autoclicker.c", false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {
            if(data.getInteractionData().isDigging()) return;

            if (samples.size() >= 120) {

                final double deviation = MathUtils.getStandardDeviation(samples);

                debug("deviation=" + deviation + " buffer=" + buffer);

                if(deviation < 5.2D) {
                    if(++buffer > 2) {
                        fail("deviation=" + deviation + " buffer=" + buffer);
                        buffer = 0;
                    }
                }else if(buffer > 0) buffer -= 0.5D;

            }

            if (ticks < 6)
                this.samples.add(ticks);


                this.ticks = 0;
        } else if (e instanceof FlyingEvent) {
            this.ticks++;
        }

    }
}
