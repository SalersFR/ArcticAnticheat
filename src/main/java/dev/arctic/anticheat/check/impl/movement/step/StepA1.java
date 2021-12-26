package dev.arctic.anticheat.check.impl.movement.step;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.packet.Packet;

public class StepA1 extends Check {

    public StepA1(PlayerData data) {
        super(data, "Step", "A1", "movement.step.a1", "Checks for invalid step height.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {
            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final boolean check = !collisionProcessor.isTeleporting() && !collisionProcessor.isLastTeleporting()
                    && collisionProcessor.isClientOnGround() && !collisionProcessor.isInVehicle() &&
                    !collisionProcessor.isOnSlime() && !collisionProcessor.isLastOnSlime();

            if(check && Math.abs(movementProcessor.getDeltaY()) >= 1.125D) {
                fail("delta=" + Math.abs(movementProcessor.getDeltaY()));
            }
        }
    }
}

