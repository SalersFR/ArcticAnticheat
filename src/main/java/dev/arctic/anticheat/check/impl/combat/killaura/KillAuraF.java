package dev.arctic.anticheat.check.impl.combat.killaura;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.utilities.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class KillAuraF extends Check {


    private final List<Long> tickSamples = new ArrayList<>(20);
    private long lastHit = 0;

    public KillAuraF(PlayerData data) {
        super(data, "KillAura", "F", "combat.killaura.f", "Checks for recurring attacks patterns.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isUseEntity()) {
            long tickDiff = System.currentTimeMillis() - lastHit;

            tickSamples.add(tickDiff);

            int similarCount = MathUtils.getRecurring(tickSamples, 50);
            boolean pattern = MathUtils.recurringPattern(tickSamples, 55, 15);

            debug("hitTickBound=" + tickDiff + " recurring=" + similarCount + " pattern=" + pattern);

            double maxBuffer = 14;

            if (data.getConnectionProcessor().getKeepAlivePing() > 150) maxBuffer = 20;
            if (data.getConnectionProcessor().getKeepAlivePing() > 215) maxBuffer = 26;

            if (similarCount > 12 && pattern) {
                if (buffer++ > maxBuffer) {
                    fail("samples=" + tickSamples.size() + " recurring=" + similarCount + " patternDetected=" + true);
                }
            } else if (buffer > 0) buffer -= 0.025D;

            if (tickSamples.size() >= 60) {
                tickSamples.clear();
                debug("samples=CLEAR");
            }

            lastHit = System.currentTimeMillis();
        }
    }
}
