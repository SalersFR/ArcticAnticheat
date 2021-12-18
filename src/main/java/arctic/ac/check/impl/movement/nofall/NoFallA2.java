package arctic.ac.check.impl.movement.nofall;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.CollisionProcessor;
import eu.salers.salty.packet.type.PacketType;

public class NoFallA2 extends Check {

    public NoFallA2(PlayerData data) {
        super(data, "NoFall", "Aé", "movement.nofall.a", "Checks if player is spoofing ground state.", false);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if(isFlyingPacket(packetType)) {
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final int mathGroundTicks = collisionProcessor.getMathGroundTicks();
            final int collAirTicks = collisionProcessor.getCollisionAirTicks();

            debug("client=" + mathGroundTicks + " coll=" + collAirTicks);


            if(collAirTicks > 16 && mathGroundTicks > 7) {
                if(++buffer > 1.25)
                    fail("client=" + mathGroundTicks + " coll=" + collAirTicks);
            } else if(buffer > 0) buffer -= 0.1D;



        }

    }
}