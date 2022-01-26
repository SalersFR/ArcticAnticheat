package dev.arctic.anticheat.check.impl.movement.flight;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.packet.Packet;

public class FlightA1 extends Check {

    public FlightA1(PlayerData data) {
        super(data, "Flight", "A1", "movement.flight.a1", "Checks for an invalid vertical movement.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();
            final MovementProcessor movementProcessor = data.getMovementProcessor();

            final double deltaY = movementProcessor.getDeltaY();
            final double lastDeltaY = movementProcessor.getLastDeltaY();

            final boolean inAir = collisionProcessor.getClientAirTicks() > 21;

            final boolean exempt = collisionProcessor.isBonkingHead() || collisionProcessor.isLastBonkingHead()
                    || collisionProcessor.isOnClimbable() || collisionProcessor.isLastOnClimbable() || collisionProcessor
                    .isInVehicle() || collisionProcessor.isNearPiston() || collisionProcessor.isLastNearPiston()
                    || collisionProcessor.isInWater() || collisionProcessor.isInLava() || collisionProcessor.isInWeb()
                    || collisionProcessor.isLastInWeb() || collisionProcessor.getPlacingTicks() <= 15 ||
                    collisionProcessor.isTeleporting() ||
                    data.getVelocityProcessor().getVelocityTicks() <= 20;


            final double expected = (lastDeltaY - 0.08) * 0.98F;
            final double threshold = movementProcessor.isLastPos() ? 0.001 : 0.0313;

            final double accuracy = Math.abs(expected - deltaY);

            if (expected >= 0.005 && !exempt && inAir) {
                if (accuracy > threshold) {
                    if (buffer < 6.25)
                        buffer++;
                    if (buffer > 4)
                        fail("acc=" + accuracy + " exp=" + expected + " delta=" + deltaY);

                } else if (buffer > 0) buffer -= 0.1D;

            }

            debug("acc=" + accuracy + " exp=" + expected + " delta=" + deltaY);


        }
    }
}

