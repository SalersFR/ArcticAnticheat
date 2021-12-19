package arctic.ac.check.impl.movement.motion;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.CollisionProcessor;
import arctic.ac.data.processor.impl.MovementProcessor;
import arctic.ac.utils.WorldUtils;
import eu.salers.salty.packet.type.PacketType;

public class MotionC extends Check {

    private int jumpTicks;

    public MotionC(PlayerData data) {
        super(data, "Motion", "C", "movement.motion.c", "Checks for fast fall modules.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_POSITION_LOOK || packetType == PacketType.IN_POSITION) {

            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final double deltaY = movementProcessor.getDeltaY();
            final boolean jumped = deltaY > 0 && collisionProcessor.getClientAirTicks() == 1;

            final int airTicks = collisionProcessor.getClientAirTicks();

            //FIXME EXEMPT FROM VELOCITY

            if (jumped)
                jumpTicks = 0;

            final boolean exempt =
                    new WorldUtils().isOnACertainBlock(data.getPlayer(), "ice")
                            || movementProcessor.isTeleported()
                            || new WorldUtils().isOnACertainBlock(data.getPlayer(), "door")
                            || new WorldUtils().isOnACertainBlock(data.getPlayer(), "slime")
                            || collisionProcessor.isWeb()
                            || collisionProcessor.isClimbable()
                            || collisionProcessor.isLiquid();

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
