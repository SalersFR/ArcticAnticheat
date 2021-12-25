package dev.arctic.anticheat.check.impl.combat.velocity;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.VelocityProcessor;
import dev.arctic.anticheat.packet.Packet;

public class VelocityA extends Check {

    public VelocityA(PlayerData data) {
        super(data, "Velocity", "A", "combat.velocity.a", "Checks for vertical knockback modifications", true);
    }


    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {

            final VelocityProcessor velocityProcessor = data.getVelocityProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final boolean exempt = collisionProcessor.isBonkingHead() || collisionProcessor.isOnClimbable()
                    || collisionProcessor.isInWater() || collisionProcessor.isInLava() || collisionProcessor.isInWeb();

            final double deltaY = data.getMovementProcessor().getDeltaY();

            if (velocityProcessor.getVelTicks() < 5 && !exempt) {
                double predictedVelocity = velocityProcessor.getVelocityY();

                double ratio = deltaY / predictedVelocity;

                if (ratio > 0 && ratio < 0.9999) {
                    if (buffer++ > 0) {
                        fail("percentage=" + (float) ratio + " deltaY=" + (float) deltaY + " predicted=" + (float) predictedVelocity);
                        buffer = 0;
                    }
                } else buffer = Math.max(0, buffer - 0.1);

                debug("percentage=" + (float) (ratio * 100.0) + ", deltaY=" + (float) deltaY + ", predicted=" + (float) predictedVelocity);

            }
        }

    }
}
