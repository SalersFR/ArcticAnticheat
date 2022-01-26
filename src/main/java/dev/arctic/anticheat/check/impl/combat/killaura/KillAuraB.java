package dev.arctic.anticheat.check.impl.combat.killaura;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CombatProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.packet.Packet;
import org.bukkit.entity.EntityType;

public class KillAuraB extends Check {

    public KillAuraB(PlayerData data) {
        super(data, "KillAura", "B", "combat.killaura.b", "Checks for invalid sprint mechanic while attacking.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isFlying()) {

            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CombatProcessor combatProcessor = data.getCombatProcessor();

            if(combatProcessor.getTarget() == null) return;

            final double deltaXZ = movementProcessor.getDeltaXZ();
            final double accel = Math.abs(deltaXZ - movementProcessor.getLastDeltaXZ());

            final boolean check = deltaXZ > 0.165 &&  combatProcessor.getTarget().getType()
                    == EntityType.PLAYER && data.getActionProcessor().isSprinting() && combatProcessor.getHitTicks() <= 2;

            if(check && accel <= 0.003) {
                if (++this.buffer > 4.5) {
                    fail("accel=" + accel);
                }

            } else this.buffer *= 0.875;
        }

    }
}
