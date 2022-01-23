package dev.arctic.anticheat.check.impl.combat.killaura;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

public class KillAuraE extends Check {


    private boolean swung, attacked;


    public KillAuraE(PlayerData data) {
        super(data, "KillAura", "E", "combat.killaura.e", "Checks for an invalid arm animation packet.", true);
    }


    @Override
    public void handle(Packet packet, long time) {
        if (packet.isArmAnimation()) {
            swung = true;
        } else if (packet.isUseEntity()) {
            if (data.getCombatProcessor().getHitTicks() <= 1)
                attacked = true;

            if (!swung && attacked)
                fail();

        } else if (packet.isFlying()) {
            attacked = swung = false;

        }


    }
}
