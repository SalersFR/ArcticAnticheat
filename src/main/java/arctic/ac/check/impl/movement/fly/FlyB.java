package arctic.ac.check.impl.movement.fly;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.CollisionProcessor;
import arctic.ac.data.processor.impl.MovementProcessor;
import eu.salers.salty.packet.type.PacketType;

public class FlyB extends Check {
    public FlyB(PlayerData data) {
        super(data, "Fly", "B", "movement.fly.b", "Checks for accel.", false);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if(packetType == PacketType.IN_POSITION_LOOK || packetType == PacketType.IN_POSITION) {

            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final double accel = Math.abs(movementProcessor.getDeltaY() - movementProcessor.getLastDeltaY());
            final boolean midAir = collisionProcessor.getCollisionAirTicks() > 11 && collisionProcessor.getClientAirTicks() > 9;

            final boolean exempt = collisionProcessor.isNearBoat() || collisionProcessor.isLiquid()
                    || collisionProcessor.isWeb() || collisionProcessor.isClimbable();

            debug("accel=" + accel);


            if (midAir && !exempt && accel <= 1.0E-5) {
                if (++buffer > 3.25)
                    fail("accel=" + accel);

            } else if (buffer > 0) buffer -= 0.1D;

        }
    }
}
