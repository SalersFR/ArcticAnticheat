package dev.arctic.anticheat.check.impl.movement.motion;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.packet.Packet;

public class MotionC extends Check {


    private int jumpTicks;

    public MotionC(PlayerData data) {
        super(data, "Motion", "C", "movement.motion.c", "Checks for fast fall modules.", true);
    }
    @Override
    public void handle(Packet packet, long time) {
        if(packet.isFlying()) {
            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final double deltaY = movementProcessor.getDeltaY();
            final boolean jumped = deltaY > 0 && collisionProcessor.getClientAirTicks() == 1;

            final int airTicks = collisionProcessor.getClientAirTicks();

            if (jumped)
                jumpTicks = 0;

            final boolean exempt = collisionProcessor.getFenceCollisions().stream().anyMatch(block ->
                    block.isFence() || block.isFenceGate() || block.isWall() || block.isDoor()) ||
                    collisionProcessor.isInWater() || collisionProcessor.isInLava() || collisionProcessor.isOnClimbable() ||
                    data.getVelocityProcessor().getVelocityTicks() <= 14;

            debug("deltaY=" + deltaY + " jumpTicks=" + jumpTicks + " airTicks=" + airTicks);

            if (airTicks >= jumpTicks && jumpTicks <= 10 && deltaY < -0.38 && !exempt) {
                if (++buffer > 2)
                    fail("fast fall jump\ndDY:" + deltaY + "\ntSJ:" + jumpTicks);

            } else if (buffer > 0) buffer -= 0.01D;

            if (airTicks > 22 && deltaY < -3.79D && !exempt) {
                if (++buffer > 2)
                    fail("fast fall \ndDY:" + deltaY);

            } else if (buffer > 0) buffer -= 0.01D;

            if (deltaY < -4.3275 && !exempt && airTicks > 0) {
                if (++buffer > 2)
                    fail("very fast fall \ndDY:" + deltaY);
            }

            jumpTicks++;

        }

    }
}
