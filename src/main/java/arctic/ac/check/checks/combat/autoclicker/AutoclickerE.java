package arctic.ac.check.checks.combat.autoclicker;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.ArmAnimationEvent;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.utils.ArcticQueue;
import arctic.ac.utils.MathUtils;

public class AutoclickerE extends Check {

    private ArcticQueue samples = new ArcticQueue<Integer>(20);

    private int ticks;
    private double lastKurt;

    public AutoclickerE(PlayerData data) {
        super(data, "Autoclicker", "E", "combat.autoclicker.e", "Checks for too low kurtosis.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {
            if (data.getInteractionData().isDigging()) return;

            double kurtosis = MathUtils.getKurtosis(samples);
            double lastKurt = this.lastKurt;
            this.lastKurt = kurtosis;
            double diff = MathUtils.hypot(kurtosis, lastKurt);
            double absDiff = Math.abs(diff);

            if (samples.size() >= 20) {
                debug("diffK=" + diff);

                if (absDiff <= 0.55D) {
                    if (++buffer > 3) {
                        fail("absDiff=" + absDiff);
                    }
                } else if (buffer > 0) buffer -= 0.25;
            }

            samples.add(this.ticks);
            debug("absDiff=" + absDiff + " buffer=" + buffer);
            this.ticks = 0;

        } else if (e instanceof FlyingEvent) {
            this.ticks++;
        }
    }
}
