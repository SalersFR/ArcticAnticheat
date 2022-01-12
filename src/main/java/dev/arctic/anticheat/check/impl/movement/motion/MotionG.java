package dev.arctic.anticheat.check.impl.movement.motion;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.utilities.PlayerUtils;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class MotionG extends Check {

    private double motionY = 0;

    public MotionG(PlayerData data) {
        super(data, "Motion", "G", "movement.motion.g", "Checks for invalid y axis moves near ground.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {
            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final boolean exempt = collisionProcessor.isBonkingHead() || collisionProcessor.isLastBonkingHead()
                    || collisionProcessor.isOnClimbable() || collisionProcessor.isLastOnClimbable() || collisionProcessor
                    .isInVehicle() || collisionProcessor.isNearPiston() || collisionProcessor.isLastNearPiston()
                    || collisionProcessor.isInWater() || collisionProcessor.isInLava() || collisionProcessor.isInWeb()
                    || collisionProcessor.isLastInWeb() || collisionProcessor.getPlacingTicks() <= 15 ||
                    collisionProcessor.isTeleporting() || data.getVelocityProcessor().getVelTicks() <= 8;

            if (exempt) return;

            final double deltaY = movementProcessor.getDeltaY();

            final boolean jumped = deltaY > 0 && collisionProcessor.isLastMathOnGround() && !collisionProcessor.isMathOnGround();
            final boolean ground = collisionProcessor.isClientOnGround() || collisionProcessor.isMathOnGround() || collisionProcessor.isCollisionOnGround();

            if (Math.abs(motionY) < 0.005)
                motionY = 0;

            if (ground)
                motionY = 0;


            if (jumped) {
                jump();
            }

            if (collisionProcessor.getMathAirTicks() >= 2) {
                if(collisionProcessor.getMathAirTicks() < 11) {
                    motionY -= 0.08D;
                    motionY *= 0.98F;
                } else {
                    motionY = (movementProcessor.getLastDeltaY() - 0.08) * 0.98F;
                }
            }

            final double offset = new Vector(0, motionY, 0).distance(new Vector(0, deltaY, 0));

            if(offset > 0.00375) {
                buffer += 0.5;
                if(buffer > 1.65)  {
                    fail("offset=" + offset);
                    buffer = 1.15;
                }


            } else if(buffer > 0) buffer -= 0.125D;


        }
    }

    protected void jump() {

        this.motionY = (double) 0.42F;

        if (data.getPlayer().hasPotionEffect(PotionEffectType.JUMP)) {
            motionY += (PlayerUtils.getPotionLevel(data.getPlayer(), PotionEffectType.JUMP) * 0.1F);
        }


    }
}
