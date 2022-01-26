package dev.arctic.anticheat.check.impl.movement.step;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.packet.Packet;

public class StepA extends Check {

    public StepA(PlayerData data) {
        super(data, "Step", "A", "movement.step.a", "Checks for invalid step height.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {
            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final boolean check = collisionProcessor.getTpTicks() >= 10
                    && collisionProcessor.getClientGroundTicks() >= 2 && !collisionProcessor.isInVehicle();

            if (check && Math.abs(movementProcessor.getDeltaY()) > 0.6D) {
                if (++buffer > 1)
                    fail("delta=" + Math.abs(movementProcessor.getDeltaY()));
            } else if(buffer > 0) buffer -= 0.005D;
        }
    }
}
