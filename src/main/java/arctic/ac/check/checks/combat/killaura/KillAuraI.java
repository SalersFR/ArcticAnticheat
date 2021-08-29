package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.UseEntityEvent;
import arctic.ac.utils.MathUtils;

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
