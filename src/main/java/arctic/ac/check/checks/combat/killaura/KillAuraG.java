package arctic.ac.check.checks.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.UseEntityEvent;
import arctic.ac.utils.MathUtils;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import java.util.ArrayList;
import java.util.List;

public class KillAuraG extends Check {

    private final List<Long> tickSamples = new ArrayList<>(20);
    private long lastHit = 0;

    public KillAuraG(PlayerData data) {
        super(data, "KillAura", "G", "combat.killaura.g", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof UseEntityEvent) {
            long tickDiff = System.currentTimeMillis() - lastHit;

            tickSamples.add(tickDiff);

            int similarCount = MathUtils.getRecurring(tickSamples, 50);
            boolean pattern = MathUtils.recurringPattern(tickSamples, 55, 15);

            debug("hitTickBound=" + tickDiff + " recurring=" + similarCount + " pattern=" + pattern);

            double maxBuffer = 14;

            if (((CraftPlayer) data.getPlayer()).getHandle().ping > 150) maxBuffer = 20;
            if (((CraftPlayer) data.getPlayer()).getHandle().ping > 215) maxBuffer = 26;

            if (similarCount > 12 && pattern) {
                if (buffer++ > maxBuffer) {
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
