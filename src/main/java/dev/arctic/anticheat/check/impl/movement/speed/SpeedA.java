package dev.arctic.anticheat.check.impl.movement.speed;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.utilities.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;

public class SpeedA extends Check {

    private int air, ground, tsJ;
    private double friction, lastFriction;


    public SpeedA(PlayerData data) {
        super(data, "Speed", "A", "movement.speed.a", "Checks if player is not applying friction.", false);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {

            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();
            final MovementProcessor movementProcessor = data.getMovementProcessor();

            final boolean onGround = collisionProcessor.isClientOnGround() || collisionProcessor.isCollisionOnGround();


            air = onGround ? 0 : Math.min(air + 1, 20);
            ground = onGround ? Math.min(ground + 1, 20) : 0;

            // deltas
            final double deltaXZ = movementProcessor.getDeltaXZ();
            final double lastDeltaXZ = movementProcessor.getLastDeltaXZ();


            // landMovementFactor
            final int speed = PlayerUtils.getPotionLevel(data.getPlayer(), PotionEffectType.SPEED);
            final int slow = PlayerUtils.getPotionLevel(data.getPlayer(), PotionEffectType.SLOW);

            double d = 0.10000000149011612;
            d += d * 0.20000000298023224 * speed;
            d += d * -0.15000000596046448 * slow;

            // Sprint desync big gay just assume they are sprinting
            d += d * 0.30000001192092896;

            float landMovementFactor = (float) d;


            // the check itself
            double prediction;
            if (ground > 2) {
                prediction = lastDeltaXZ * 0.91f * getBlockFriction(data) + landMovementFactor;
            } else if (air == 1) {
                prediction = lastDeltaXZ * 0.91f + 0.2f + landMovementFactor;
            } else if (ground == 2) {
                prediction = lastDeltaXZ * 0.91f + landMovementFactor;
            } else {
                prediction = lastDeltaXZ * 0.91f + 0.026f;
            }
            if (prediction < data.getPlayer().getWalkSpeed() + 0.02 * (speed + 1))
                prediction = data.getPlayer().getWalkSpeed() + 0.02 * (speed + 1);

            // very lazy patch for a false flag
            if (ground > 1) {
                this.lastFriction = this.friction;
                this.friction = getBlockFriction(data) * 0.91f;
            }


           


            if (friction < lastFriction)
                prediction += landMovementFactor * 1.25;

             //jump handling
            final boolean jumped = movementProcessor.getDeltaY() > 0 && deltaXZ >= 0.326 && !collisionProcessor.isClientOnGround();

            if (jumped)
                tsJ = 0;
            else tsJ++;

            if(tsJ == 8)
                prediction += 0.3125;
            


            final boolean exempt = collisionProcessor.getFenceCollisions()
                    .stream().anyMatch(block -> block.isFence() || block.isFenceGate() || block.isWall() || block.isDoor()) ||
                    collisionProcessor.isNearSlab() || collisionProcessor.isNearStairs()
                    || collisionProcessor.isLastTeleporting() || collisionProcessor.isTeleporting() || data.getVelocityProcessor().getVelocityTicks() <= 11;

            debug("limit=" + prediction + "\ndelta=" + deltaXZ + "\nexempt=" + exempt + "\nbuffer=" + buffer);

            if (collisionProcessor.isBonkingHead() || collisionProcessor.isLastBonkingHead())
                prediction += 0.4D;

            if (collisionProcessor.isOnIce() || collisionProcessor.isLastOnIce()) {
                prediction += 0.525D;
            }

            //stairs & slab accounting:
            if (movementProcessor.getDeltaY() == 0.5D)
                prediction += 0.25D;

            if (movementProcessor.getDeltaY() == 0.0D) {
                //random accounting idfk
                if (deltaXZ > prediction)
                    prediction += 0.05;
            }

            //join fix
            if (deltaXZ >= 15)
                return;

            // flag
            if (deltaXZ > prediction && !exempt) {
                if (movementProcessor.getDeltaY() == 0) buffer -= 0.2D;
                if(movementProcessor.getDeltaY() != 0.0 && tsJ > 60) buffer -= 0.85;
                if (++buffer >= 5)
                    fail("limit=" + prediction + " delta=" + deltaXZ);
            } else if (this.buffer > 0) buffer -= 0.05D;


        }
    }


    public float getBlockFriction(PlayerData data) {
        String block = data.getPlayer().getLocation().add(0, -1, 0).getBlock().getType().name().toLowerCase();
        return block.equals("blue ice") ? 0.989f : block.contains("ice") ? 0.98f : block.equals("slime") ? 0.8f : 0.6f;
    }
}
