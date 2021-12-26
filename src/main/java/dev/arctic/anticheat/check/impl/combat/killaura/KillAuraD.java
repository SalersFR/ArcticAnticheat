package dev.arctic.anticheat.check.impl.combat.killaura;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;

public class KillAuraD extends Check {

    private int attacksWithoutSwing;

    public KillAuraD(PlayerData data) {
        super(data, "KillAura", "D", "combat.killaura.d", "Checks if player is not swinging when attacking.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isAttack()) {
            if(++attacksWithoutSwing > 6)
                fail("attacks=" + attacksWithoutSwing);
        } else if(packet.isArmAnimation()) {
            attacksWithoutSwing = 0;
        }

    }
}
