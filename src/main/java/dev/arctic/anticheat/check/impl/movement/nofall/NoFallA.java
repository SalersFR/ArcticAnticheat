package dev.arctic.anticheat.check.impl.movement.nofall;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.packet.Packet;

public class NoFallA extends Check {

    public NoFallA(PlayerData data) {
        super(data, "NoFall", "A", "movement.nofall.a", "Checks if player is spoofing ground state.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final boolean exempt = collisionProcessor.isNearCarpet() || collisionProcessor.isLastNearCarpet() ||
                    collisionProcessor.isNearPiston() || collisionProcessor.isLastNearPiston() ||
                    data.getClickProcessor().getPlaceTicks() <= 15 || collisionProcessor.isOnClimbable()
                    || data.getVelocityProcessor().getVelocityTicks() <= 10 || collisionProcessor.isInWeb();

            if (!collisionProcessor.isCollisionOnGround() && !collisionProcessor.isLastCollisionOnGround()
                    && collisionProcessor.isClientOnGround() && collisionProcessor.isLastClientOnGround()
                    &&  !exempt) {
                if (++buffer > 1)
                    fail("flying ground spoofed");
            } else if (buffer > 0) buffer -= 0.1D;
        }

    }
}
