package dev.arctic.anticheat.check.impl.movement.flight;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.packet.Packet;

public class FlightC extends Check {

    public FlightC(PlayerData data) {
        super(data, "Flight", "C", "movement.flight.c", "Checks for jump in mid-air", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {

            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();
            final MovementProcessor movementProcessor = data.getMovementProcessor();

            final double deltaY = movementProcessor.getDeltaY();
            final double lastDeltaY = movementProcessor.getLastDeltaY();

            final boolean inAir = collisionProcessor.getClientAirTicks() >= 9;

            final boolean exempt = collisionProcessor.isBonkingHead() || collisionProcessor.isLastBonkingHead()
                    || collisionProcessor.isOnClimbable() || collisionProcessor.isLastOnClimbable() || collisionProcessor
                    .isInVehicle() || collisionProcessor.isNearPiston() || collisionProcessor.isLastNearPiston()
                    || collisionProcessor.isInWater() || collisionProcessor.isInLava() || collisionProcessor.isInWeb()
                    || collisionProcessor.isLastInWeb() || collisionProcessor.getPlacingTicks() <= 15;

            if (deltaY > lastDeltaY && !exempt && inAir) {
                if (++buffer > 2)
                    fail("delta=" + deltaY + " last=" + lastDeltaY);
            } else if(buffer > 0) buffer -= 0.1D;


        }
    }
}
