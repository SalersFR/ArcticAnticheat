package arctic.ac.check.checks.combat.autoclicker;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.ArmAnimationEvent;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.utils.ArcticQueue;
import arctic.ac.utils.MathUtils;

public class AutoclickerA extends Check {

    private ArcticQueue<Integer> samples = new ArcticQueue<Integer>(20);


    private int ticks;

    /**
     * Checking for mistake in clicker randomization
     */


    public AutoclickerA(PlayerData data) {
        super(data, "Autoclicker", "A", "combat.autoclicker.a", "Checks for too low randomization.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {
            if (data.getInteractionData().isDigging()) return;

            if (samples.size() >= 20) {

                final double stdDeviation = MathUtils.getStandardDeviation(samples);

                if (stdDeviation < 1.2) {
                    buffer += (1.2 - stdDeviation);
                    if (buffer > 3.25)
                        fail("std=" + stdDeviation);
                } else if (buffer > 0) buffer -= (float) (10 / 3D);

                debug("std=" + stdDeviation + " buffer=" + buffer);


            }


            if (ticks < 7)
                samples.add(ticks);


            this.ticks = 0;

        } else if (e instanceof FlyingEvent) {
            this.ticks++;
        }
    }
}
