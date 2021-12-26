package dev.arctic.anticheat.check.impl.combat.killaura;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CombatProcessor;
import dev.arctic.anticheat.packet.Packet;

public class KillAuraC extends Check {

    private int ticks, lastID;

    public KillAuraC(PlayerData data) {
        super(data, "KillAura", "C", "combat.killaura.c", "Checks if player is attacking two entities at once.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isAttack()) {
            final CombatProcessor combatProcessor = data.getCombatProcessor();
            final int id = combatProcessor.getTarget().getEntityId();
            if(id != lastID) {
                ticks++;
                if(ticks > 1) {
                    fail("ticks=" + ticks);
                }
            }

            lastID = id;
        } else if(packet.isFlying()) {
            ticks = 0;
        }
    }
}
