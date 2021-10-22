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

public class AutoclickerD extends Check {

    private int ticks;
    private double lastDifference;
    private ArcticQueue<Integer> samples = new ArcticQueue(30);

    public AutoclickerD(final PlayerData data) {
        super(data, "Autoclicker", "D", "combat.autoclicker.d", "Checks for consistency.",false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {
            if (data.getInteractionData().isDigging()) return;

            if (this.samples.size() >= 30) {

                final Pair<List<Double>, List<Double>> outliers = MathUtils.getOutliers(samples);
                final int total = outliers.getX().size() + outliers.getY().size();

                final double deviation = MathUtils.getStandardDeviation(samples);
                final int sames = MathUtils.getSames(samples);

                debug("dev=" + deviation + " outliers=" + total + " sames=" + sames);

                if ((deviation < 1.525D || total <= 3) && sames > 28) {
                    if (++buffer > 3)
                        fail( " outliers=" + outliers + " deviation=" + deviation + " sames=" + sames);
                } else if (buffer > 0) buffer -= 0.25D;
            }


            samples.add(this.ticks);
            this.ticks = 0;

        } else if (e instanceof FlyingEvent) {
            this.ticks++;
        }
    }
}
