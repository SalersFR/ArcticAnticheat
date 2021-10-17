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

    public AutoclickerE(PlayerData data) {
        super(data, "Autoclicker", "E", "combat.autoclicker.e", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {
            if (data.getInteractionData().isDigging()) return;

            double kurtosis = MathUtils.getKurtosis(samples);

            if (samples.size() >= 20) {
                debug("kurt=" + kurtosis);

                if (kurtosis <= 3.6) {
                    if (++buffer > 1) {
                        fail("kurt=" + kurtosis);
                    }
                } else if (buffer > 0) buffer -= 0.25;
            }

            samples.add(this.ticks);
            this.ticks = 0;

        } else if (e instanceof FlyingEvent) {
            this.ticks++;
        }
    }
}
