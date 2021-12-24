package dev.arctic.anticheat.check.impl.movement.motion;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.utilities.PlayerUtils;
import org.bukkit.potion.PotionEffectType;

public class MotionA extends Check {

    public MotionA(PlayerData data) {
        super(data, "Motion", "A", "movement.motion.a", "Checks for an invalid jump height.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if(packet.isFlying()) {

            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final double deltaY = movementProcessor.getDeltaY();
            final double prediction = 0.42F;

            final double fixedPrediction = data.getPlayer().hasPotionEffect(PotionEffectType.JUMP) ? prediction
                    + PlayerUtils.getPotionLevel(data.getPlayer(), PotionEffectType.JUMP) : prediction;

            final boolean slime = collisionProcessor.isOnSlime() || collisionProcessor.isLastOnSlime() || collisionProcessor.isLastOnGroundSlime();
            final boolean jumped = deltaY > 0 && collisionProcessor.isLastMathOnGround() && !collisionProcessor.isMathOnGround();

            final boolean exempt = slime || collisionProcessor.isInWater() || collisionProcessor.isInLava() || collisionProcessor.isBonkingHead();

            if(!exempt && jumped && deltaY != fixedPrediction) {
                if(++buffer > 0.75)
                    fail("delta=" + deltaY + " pred=" + fixedPrediction);

            } else if(buffer > 0) buffer -= 0.1D;
        }
    }
}
