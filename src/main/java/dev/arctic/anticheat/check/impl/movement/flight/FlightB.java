package dev.arctic.anticheat.check.impl.movement.flight;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.packet.Packet;

public class FlightB extends Check {

    public FlightB(PlayerData data) {
        super(data, "Flight", "B", "movement.flight.b", "Checks for invalid accel.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {

            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();
            final MovementProcessor movementProcessor = data.getMovementProcessor();

            final double deltaY = movementProcessor.getDeltaY();
            final double lastDeltaY = movementProcessor.getLastDeltaY();

            final double accel = Math.abs(deltaY - lastDeltaY);
            final boolean inAir = collisionProcessor.getMathAirTicks() >= 8;

            final boolean exempt = collisionProcessor.isBonkingHead() || collisionProcessor.isLastBonkingHead()
                    || collisionProcessor.isOnClimbable() || collisionProcessor.isLastOnClimbable() || collisionProcessor
                    .isInVehicle() || collisionProcessor.isNearPiston() || collisionProcessor.isLastNearPiston()
                    || collisionProcessor.isInWater() || collisionProcessor.isInLava() || collisionProcessor.isInWeb()
                    || collisionProcessor.isLastInWeb() || collisionProcessor.getPlacingTicks() <= 15;

            if (accel <= 0.00325 && !exempt && inAir) {
                if (++buffer > 2)
                    fail("accel=" + accel);
            } else if(buffer > 0) buffer -= 0.1D;


        }
    }
}
