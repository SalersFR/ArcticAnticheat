package polar.ac.check.checks.combat.autoclicker;

import org.bukkit.Bukkit;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.ArmAnimationEvent;
import polar.ac.event.client.FlyingEvent;
import polar.ac.utils.MathUtils;
import polar.ac.utils.PolarQueue;

public class AutoclickerB extends Check {

    private PolarQueue samples = new PolarQueue<Integer>(120);


    private int ticks;
    private double lastDiff;

    public AutoclickerB(PlayerData data) {
        super(data, "Autoclicker", "B", "combat.autoclicker.b", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ArmAnimationEvent) {

            final double kurtosis = MathUtils.getKurtosis(samples);
            final double stdDeviation = MathUtils.getStandardDeviation(samples);





            if (samples.size() >= 120) {
                final double diff = Math.abs(kurtosis - stdDeviation);
                final double result = Math.abs(diff - lastDiff);
                debug("lastDiff=" + lastDiff + " diff=" + diff + " result=" + result);

                if(result < 2.5D) {
                    if(buffer < 8) buffer += result;

                    if(buffer > 3) {
                        fail("result=" + result);
                    }
                }else if(buffer > 0) buffer --;

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
