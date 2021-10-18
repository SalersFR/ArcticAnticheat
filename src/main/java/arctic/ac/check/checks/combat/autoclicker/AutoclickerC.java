package arctic.ac.check.checks.combat.autoclicker;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.ArmAnimationEvent;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.utils.ArcticQueue;
import arctic.ac.utils.MathUtils;

public class AutoclickerC extends Check {

    private final ArcticQueue samples = new ArcticQueue<Integer>(60);
    private int ticks;

    public AutoclickerC(PlayerData data) {
        super(data, "Autoclicker", "C", "combat.autoclicker.c", "Checks for too low deviation.", false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {
            if (data.getInteractionData().isDigging()) return;

            if (samples.size() >= 60) {

                final double deviation = MathUtils.getStandardDeviation(samples);

                debug("deviation=" + deviation + " buffer=" + buffer);

                if (deviation < 2.1D) {
                    if (++buffer > 2) {
                        fail("deviation=" + deviation + " buffer=" + buffer);
                        buffer = 0;
                    }
                } else if (buffer > 0) buffer -= 0.5D;

            }

            if (ticks < 6)
                this.samples.add(ticks);


            this.ticks = 0;
        } else if (e instanceof FlyingEvent) {
            this.ticks++;
        }

    }
}
