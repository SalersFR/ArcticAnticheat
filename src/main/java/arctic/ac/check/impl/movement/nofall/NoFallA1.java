package arctic.ac.check.impl.movement.nofall;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.CollisionProcessor;
import eu.salers.salty.packet.type.PacketType;

public class NoFallA1 extends Check {

    public NoFallA1(PlayerData data) {
        super(data, "NoFall", "A1", "movement.nofall.a", "Checks if player is spoofing ground state.", false);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if(isFlyingPacket(packetType)) {
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final boolean clientGround = collisionProcessor.isClientGround();
            final boolean mathGround = collisionProcessor.isMathGround();

            debug("client=" + clientGround + " coll=" + mathGround);

            if(clientGround != mathGround) { //wtf yeah
                if(++buffer > 3.25) {
                    // fail("client=" + clientGround + " math=" + mathGround);
                }
            } else if(buffer > 0) buffer -= 0.1D;



        }

    }
}