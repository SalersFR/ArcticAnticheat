package dev.arctic.anticheat.check.impl.movement.nofall;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.packet.Packet;

public class NoFallA1 extends Check {

    public NoFallA1(PlayerData data) {
        super(data, "NoFall", "A1", "movement.nofall.a", "Checks if player is spoofing ground state.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            if (!collisionProcessor.isCollisionOnGround() && !collisionProcessor.isLastCollisionOnGround()
                    && collisionProcessor.isMathOnGround() && collisionProcessor.isLastMathOnGround()) {
                if (++buffer > 1)
                    fail("math ground spoofed");
            } else if (buffer > 0) buffer -= 0.1D;
        }

    }
}
