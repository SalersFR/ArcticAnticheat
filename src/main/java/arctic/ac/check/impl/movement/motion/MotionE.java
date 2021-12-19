package arctic.ac.check.impl.movement.motion;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.CollisionProcessor;
import arctic.ac.data.processor.impl.MovementProcessor;
import eu.salers.salty.packet.type.PacketType;

public class MotionE extends Check {
    public MotionE(PlayerData data) {
        super(data, "Motion", "E", "movement.motion.e", "Checks for proper accel mechanics.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_POSITION_LOOK || packetType == PacketType.IN_POSITION) {

            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final double accelXZ = Math.abs(movementProcessor.getDeltaXZ() - movementProcessor.getLastDeltaXZ()) * 100;

            final boolean exempt = movementProcessor.isTeleported() || collisionProcessor.isNearBoat() || collisionProcessor.isClimbable();

            if (accelXZ <= .0001D && data.getRotationProcessor().getDeltaYaw() > 3.25F && !exempt && movementProcessor.getDeltaXZ() > 0.2325D) {
                if (++buffer > 2)
                    fail("acc=" + accelXZ);
            } else if (buffer > 0) buffer -= 0.05D;


        }


    }
}
