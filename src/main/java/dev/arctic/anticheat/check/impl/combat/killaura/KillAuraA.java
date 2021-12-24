package dev.arctic.anticheat.check.impl.combat.killaura;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;
import org.bukkit.Bukkit;

public class KillAuraA extends Check {

    private long lastFlying, lastFlyingDelay;
    private double average = 50;
    private int hits;

    public KillAuraA(PlayerData data) {
        super(data, "KillAura", "A", "combat.killaura.a", "Checks for invalid delay between attack and flying packet", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isUseEntity()) {

            final long delta = Math.abs(System.currentTimeMillis() - this.lastFlying);

            average = ((average * 14) + delta) / 15;

            debug("elapsed=" + delta + " current=" + System.currentTimeMillis() + " last=" + lastFlying);

            if (lastFlyingDelay > 10L && lastFlyingDelay < 90L) {
                if (average < 5 && hits++ > 10) {
                    fail("delta=" + average);
                    average = 5;
                }


            }
        } else if (packet.isFlying()) {
            this.lastFlyingDelay = System.currentTimeMillis() - lastFlying;
            this.lastFlying = System.currentTimeMillis();
        }

    }
}
