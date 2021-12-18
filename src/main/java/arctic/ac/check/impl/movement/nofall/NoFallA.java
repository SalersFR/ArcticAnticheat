package arctic.ac.check.impl.movement.nofall;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.CollisionProcessor;
import eu.salers.salty.packet.type.PacketType;

public class NoFallA extends Check {

    public NoFallA(PlayerData data) {
        super(data, "NoFall", "A", "movement.nofall.a", "Checks if player is spoofing ground state.", false);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if(isFlyingPacket(packetType)) {
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final int clientGroundTicks = collisionProcessor.getClientGroundTicks();
            final int collAirTicks = collisionProcessor.getCollisionAirTicks();

            debug("client=" + clientGroundTicks + " coll=" + collAirTicks);


            if(collAirTicks > 16 && clientGroundTicks > 5) {
                if(++buffer > 1.25)
                    fail("client=" + clientGroundTicks + " coll=" + collAirTicks);
            } else if(buffer > 0) buffer -= 0.1D;



        }

    }
}
