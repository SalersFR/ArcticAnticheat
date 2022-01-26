package dev.arctic.anticheat.check.impl.movement.nofall;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.packet.Packet;

public class NoFallA1 extends Check {

    public NoFallA1(PlayerData data) {
        super(data, "NoFall", "A1", "movement.nofall.a1", "Checks if player is spoofing ground state.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            if (!collisionProcessor.isClientOnGround() && !collisionProcessor.isLastClientOnGround()
                    && collisionProcessor.getMathGroundTicks() >= 12 &&
                    !collisionProcessor.isOnClimbable() && data.getVelocityProcessor().getVelocityTicks() > 20 &&
                    collisionProcessor.getTpTicks() >= 4) {
                if (++buffer > 1)
                    fail("math ground spoofed");
            } else if (buffer > 0) buffer -= 0.1D;
        }

    }
}
