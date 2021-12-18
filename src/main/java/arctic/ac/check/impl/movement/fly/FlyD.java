package arctic.ac.check.impl.movement.fly;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.CollisionProcessor;
import arctic.ac.data.processor.impl.MovementProcessor;
import eu.salers.salty.packet.type.PacketType;

public class FlyD extends Check {

    public FlyD(PlayerData data) {
        super(data, "Fly", "D", "movement.fly.d", "Checks rounded movement values.", false);
    }
    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_POSITION_LOOK || packetType == PacketType.IN_POSITION) {

            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final double deltaY = Math.abs(movementProcessor.getDeltaY());



            final boolean midAir = collisionProcessor.getCollisionAirTicks() > 12 || collisionProcessor.getClientAirTicks() > 13;

            final boolean exempt = collisionProcessor.isNearBoat() || collisionProcessor.isLiquid()
                    || collisionProcessor.isWeb() || collisionProcessor.isClimbable();

            final boolean round = deltaY % 1.D == 0 || deltaY % 1.5 == 0 || deltaY % 0.5 == 0;

            debug("delta=" + deltaY);

            if (round && midAir && !exempt && deltaY > 0) {
                if (++buffer > 3.25)
                    fail("delta=" + deltaY);

            } else if (buffer > 0) buffer -= 0.1D;

        }
    }
}
