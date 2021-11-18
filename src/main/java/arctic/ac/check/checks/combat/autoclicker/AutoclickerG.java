package arctic.ac.check.checks.combat.autoclicker;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.ArmAnimationEvent;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.utils.ArcticQueue;
import arctic.ac.utils.MathUtils;

public class AutoclickerG extends Check {

    private final ArcticQueue<Integer> samples = new ArcticQueue(20);
    private final ArcticQueue<Double> pastKurtosis = new ArcticQueue(3);
    private int ticks;

    public AutoclickerG(PlayerData data) {
        super(data, "Autoclicker", "G", "combat.autoclicker.g", "Checks for impossibles clicking samples.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {
            if (data.getInteractionData().isDigging()) return;

            if (this.samples.size() >= 20) {
                final int sames = MathUtils.getSames(this.samples);
                final double kurtosis = MathUtils.getKurtosis(this.samples);

                this.pastKurtosis.add(kurtosis);


                if (pastKurtosis.size() >= 3) {
                    final double kurtosisVariance = MathUtils.getVariance(pastKurtosis);
                    debug("sames=" + sames + " kurtosisVariance=" + kurtosisVariance);

                    if ((kurtosisVariance < 1.0E-7 || Double.isNaN(kurtosisVariance) || kurtosisVariance < 1.25D) && sames > 18) {
                        if (++buffer > 1)
                            fail("sames=" + sames + " kurtosisVariance=" + kurtosisVariance);
                    } else if (buffer > 0) buffer -= 0.5D;
                }


            }

            this.samples.add(ticks * 50);
            this.ticks = 0;

        } else if (e instanceof FlyingEvent) {
            this.ticks++;

        }

    }
}
