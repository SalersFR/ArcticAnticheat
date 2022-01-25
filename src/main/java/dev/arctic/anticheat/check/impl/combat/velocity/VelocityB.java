package dev.arctic.anticheat.check.impl.combat.velocity;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.VelocityProcessor;
import dev.arctic.anticheat.packet.Packet;

public class VelocityB extends Check {

    public VelocityB(PlayerData data) {
        super(data, "Velocity", "B", "combat.velocity.b",
                "Checks for horizontal knockback modifications.", true);
    }


    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {

            final VelocityProcessor velocityProcessor = data.getVelocityProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final boolean exempt = collisionProcessor.isBonkingHead() || collisionProcessor.isOnClimbable()
                    || collisionProcessor.isInWater() || collisionProcessor.isInLava() || collisionProcessor.isInWeb();

            final double deltaXZ = data.getMovementProcessor().getDeltaXZ();

            if (velocityProcessor.getVelocityTicks() == 1
                    && !exempt) {
                double predictedVelocity = velocityProcessor.getPredictedVelocityH();

                double ratio = deltaXZ / predictedVelocity;

                if (ratio > 0 && ratio < 0.995) {
                    if (buffer++ > 1) {
                        fail("percentage=" + (float) ratio + " deltaXZ=" + (float) deltaXZ + " predicted=" + (float) predictedVelocity);
                        buffer = 0;
                    }
                } else buffer = Math.max(0, buffer - 0.1);

                debug("percentage=" + (float) (ratio * 100.0) + ", deltaXZ=" + (float) deltaXZ + ", predicted=" + (float) predictedVelocity);

            }
        }

    }
}
