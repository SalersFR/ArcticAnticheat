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
        // TODO
    }
}
