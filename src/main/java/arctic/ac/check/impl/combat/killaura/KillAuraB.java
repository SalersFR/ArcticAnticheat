package arctic.ac.check.impl.combat.killaura;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.CombatProcessor;
import arctic.ac.data.processor.impl.MovementProcessor;
import eu.salers.salty.packet.type.PacketType;
import org.bukkit.entity.EntityType;

public class KillAuraB extends Check {

    public KillAuraB(PlayerData data) {
        super(data, "KillAura", "B", "combat.killaura.b", "Checks for invalid sprint mechanic while attacking.", false);
    }

    @Override
    public void handle(Object packet, PacketType packetType) {
        if (packetType == PacketType.IN_POSITION || packetType == PacketType.IN_POSITION_LOOK) {

            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CombatProcessor combatProcessor = data.getCombatProcessor();

            if (combatProcessor.getTarget() == null) return;

            final double accelXZ = Math.abs(movementProcessor.getDeltaXZ() - movementProcessor.getLastDeltaXZ());

            if (combatProcessor.getHitTicks() <= 2 && data.getPlayer().isSprinting() && accelXZ <= 0.001D &&
                    movementProcessor.getDeltaXZ() >= 0.17 && combatProcessor.getTarget().getType() == EntityType.PLAYER) {
                if (++this.buffer > 4.5) {
                    fail("xzAccel=" + accelXZ);
                }

            } else this.buffer *= 0.8;

        }

    }

}


