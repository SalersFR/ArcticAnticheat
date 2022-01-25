package dev.arctic.anticheat.check.impl.movement.motion;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.packet.Packet;

public class MotionD extends Check {

    public MotionD(PlayerData data) {
        super(data, "Motion", "D", "movement.motion.d", "Checks for invalid y movement near ground.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isFlying()) {
            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final double deltaY = movementProcessor.getDeltaY();
            final double lastDeltaY = movementProcessor.getLastDeltaY();

            final boolean exempt = collisionProcessor.isInWater() || collisionProcessor.isInLava()
                    || collisionProcessor.isNearBoat() ||
                    collisionProcessor.isBonkingHead()|| collisionProcessor.isOnIce() ||
                    collisionProcessor.isLastOnIce() || collisionProcessor.isInWeb() ||
                    data.getVelocityProcessor().getVelocityTicks() <= 12;


            if (deltaY < 0 && Math.abs(deltaY) == lastDeltaY && lastDeltaY > 0.2F
                    && collisionProcessor.getClientGroundTicks() >= 1 && !exempt) {
                if(deltaY == 0.5 || lastDeltaY == 0.5) buffer = 0;
                if (++buffer > 1.75)
                    fail("same pos/neg motion\ndelta=" + deltaY + " last=" + lastDeltaY);

            } else if (buffer > 0) buffer -= 0.0025D;

            if (deltaY <= 0.42f && lastDeltaY >= 0.42f && collisionProcessor.getClientGroundTicks() >= 1 && !exempt) {
                if(deltaY == 0.5 || lastDeltaY == 0.5) buffer = 0;
                if(deltaY == 0) buffer /= 2;
                if (++buffer > 2.25)
                    fail("jump (or higher) reversed motion\ndelta=" + deltaY + " last=" + lastDeltaY);

            } else if (buffer > 0) buffer -= 0.005D;

        }

    }
}
