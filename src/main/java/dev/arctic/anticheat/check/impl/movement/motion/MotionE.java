package dev.arctic.anticheat.check.impl.movement.motion;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.packet.Packet;

public class MotionE extends Check {

    public MotionE(PlayerData data) {
        super(data, "Motion", "E", "movement.motion.e", "Checks for proper accel mechanics.", true);
    }
    @Override
    public void handle(Packet packet, long time) {
        if(packet.isFlying()) {
            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final double accelXZ = Math.abs(movementProcessor.getDeltaXZ() - movementProcessor.getLastDeltaXZ()) * 100;

            final boolean exempt = collisionProcessor.isTeleporting() || collisionProcessor.isNearBoat() || collisionProcessor.isOnClimbable();

            if (accelXZ <= .0001D && data.getRotationProcessor().getDeltaYaw() > 3.25F && !exempt && movementProcessor.getDeltaXZ() > 0.2325D) {
                if (++buffer > 2)
                    fail("acc=" + accelXZ);
            } else if (buffer > 0) buffer -= 0.05D;
        }

    }
}
