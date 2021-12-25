package dev.arctic.anticheat.check.impl.movement.flight;

import com.comphenix.packetwrapper.WrapperPlayClientFlying;
import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.packet.Packet;
import org.bukkit.Bukkit;

public class FlightA extends Check {

    public FlightA(PlayerData data) {
        super(data, "Flight","A", "movement.flight.a", "Checks for an invalid vertical movement.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isPosition() || packet.isPosLook()) {

            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();
            final MovementProcessor movementProcessor = data.getMovementProcessor();

            final double deltaY = movementProcessor.getDeltaY();
            final double lastDeltaY = movementProcessor.getLastDeltaY();

            final double prediction = (lastDeltaY - 0.08F) * 0.98F;
            final double fixedPrediction = Math.abs(prediction) <= 0.005 ? 0 : prediction;


            final boolean inAir = collisionProcessor.getClientAirTicks() >= 6;

            final boolean exempt = collisionProcessor.isBonkingHead() || collisionProcessor.isLastBonkingHead()
                    || collisionProcessor.isOnClimbable() || collisionProcessor.isLastOnClimbable() || collisionProcessor
                    .isInVehicle() || collisionProcessor.isNearPiston() || collisionProcessor.isLastNearPiston()
                    || collisionProcessor.isInWater() || collisionProcessor.isInLava() || collisionProcessor.isInWeb()
                    || collisionProcessor.isLastInWeb() || collisionProcessor.getPlacingTicks() <= 15;

            //TODO EXEMPT FROM VELOCITY


            final double difference = Math.abs(fixedPrediction - deltaY);
            final double threshold = movementProcessor.isLastPos() ? 0.001 : 0.0305 + 1E-8;

            debug("air=" + inAir + "\nexempt=" + exempt +" \npred=" + fixedPrediction +"\ndelta=" + deltaY + "\ndiff=" + difference + " \nthreshold=" + threshold );;

            if(difference > threshold && !exempt && inAir) {
                if(prediction == 0) buffer -= 1.25D;
                if(++buffer > 5)
                    fail("diff=" + difference + " threshold=" + threshold);
            } else if(buffer > 0) buffer -= 0.125D;

        }
    }
}
