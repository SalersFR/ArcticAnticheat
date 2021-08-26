package polar.ac.check.checks.combat.killaura;

import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.UseEntityEvent;
import polar.ac.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class KillAuraI extends Check {

    private final List<Long> tickSamples = new ArrayList<>(20);
    private long lastHit = 0;

    public KillAuraI(PlayerData data) {
        super(data, "KillAura", "I", "combat.killaura.i", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {
            long tickDiff = System.currentTimeMillis() - lastHit;

            tickSamples.add(tickDiff);

            int similarCount = MathUtils.getRecurring(tickSamples, 50);
            boolean pattern = MathUtils.recurringPattern(tickSamples, 55, 15);

            debug("hitTickBound=" + tickDiff + " recurring=" + similarCount + " pattern=" + pattern);

            if (similarCount > 7 && pattern) {
                if (buffer++ >= 7) {
                    fail("samples=" + tickSamples.size() + " recurring=" + similarCount + " patternDetected=" + pattern);
                }
            }

            if (tickSamples.size() >= 60) {
                tickSamples.clear();
                debug("samples=CLEAR");
            }

            lastHit = System.currentTimeMillis();
        }
    }
}
