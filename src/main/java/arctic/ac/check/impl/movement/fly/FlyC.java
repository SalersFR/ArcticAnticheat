package arctic.ac.check.impl.movement.fly;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.CollisionProcessor;
import arctic.ac.data.processor.impl.MovementProcessor;
import eu.salers.salty.packet.type.PacketType;

public class FlyC extends Check {

    public FlyC(PlayerData data) {
        super(data, "Fly", "C", "movement.fly.c", "Checks air jump modules.", false);
    }


    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_POSITION_LOOK || packetType == PacketType.IN_POSITION) {

            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final double deltaY = movementProcessor.getDeltaY();
            final double lastDeltaY = movementProcessor.getLastDeltaY();

            final boolean midAir = collisionProcessor.getCollisionAirTicks() > 11 && collisionProcessor.getClientAirTicks() > 9;

            final boolean exempt = collisionProcessor.isNearBoat() || collisionProcessor.isLiquid()
                    || collisionProcessor.isWeb() || collisionProcessor.isClimbable();

            debug("delta=" + deltaY + " last=" + lastDeltaY);

            if (deltaY > lastDeltaY &&midAir && !exempt) {
                if (++buffer > 3.25)
                    fail("delta=" + deltaY + " last=" + lastDeltaY);

            } else if (buffer > 0) buffer -= 0.1D;

        }
    }
}
