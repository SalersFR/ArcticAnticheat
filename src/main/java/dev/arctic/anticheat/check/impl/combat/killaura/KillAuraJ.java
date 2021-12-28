package dev.arctic.anticheat.check.impl.combat.killaura;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class KillAuraJ extends Check {

    public KillAuraJ(PlayerData data) {
        super(data, "KillAura", "J", "combat.killaura.j", "Checks for glitches in hitboxes.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isUseEntity()) {
            if (data.getCombatProcessor().getTarget() != null) {
                Vector vec = data.getCombatProcessor().getTarget().getLocation().toVector();

                debug("x=" + Math.abs(vec.getX()) + " y=" + Math.abs(vec.getY()) + " z=" + Math.abs(vec.getZ()));

                if (Math.abs(vec.getX()) > 0.4f || Math.abs(vec.getY()) > 1.902 || Math.abs(vec.getZ()) > 0.4f
                        && data.getCombatProcessor().getTarget() instanceof Player) {
                    if (++buffer > 0)
                        fail();

                } else if (buffer > 0) buffer -= 0.025D;

            }
        }
    }
}
