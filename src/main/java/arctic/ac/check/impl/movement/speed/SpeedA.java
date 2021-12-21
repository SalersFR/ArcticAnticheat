package arctic.ac.check.impl.movement.speed;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.CollisionProcessor;
import arctic.ac.data.processor.impl.MovementProcessor;
import arctic.ac.utils.ArcticPotionEffect;
import arctic.ac.utils.PlayerUtils;
import arctic.ac.utils.WorldUtils;
import eu.salers.salty.packet.type.PacketType;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;

public class SpeedA extends Check {

    private int air, ground;
    private double friction, lastFriction;

    public SpeedA(PlayerData data) {
        super(data, "Speed", "A", "movement.speed.a", "Check if player is following minecraft's friction rule", false);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_POSITION || packetType == PacketType.IN_POSITION_LOOK) {
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();
            final MovementProcessor movementProcessor = data.getMovementProcessor();

            final boolean onGround = collisionProcessor.isClientGround();


            air = onGround ? 0 : Math.min(air + 1, 20);
            ground = onGround ? Math.min(ground + 1, 20) : 0;

            // deltas
            final double deltaXZ = movementProcessor.getDeltaXZ();
            final double lastDeltaXZ = movementProcessor.getLastDeltaXZ();


            // landMovementFactor

            final Optional<ArcticPotionEffect> speedEffect = data.getStatusProcessor().getPotionEffects().stream().filter(effect ->
                    effect.getType() == PotionEffectType.SPEED).findAny();

            final Optional<ArcticPotionEffect> slowEffect = data.getStatusProcessor().getPotionEffects().stream().filter(effect ->
                    effect.getType() == PotionEffectType.SLOW).findAny();

            final int speed = speedEffect.map(ArcticPotionEffect::getLevel).orElse(0);
            final int slow = slowEffect.map(ArcticPotionEffect::getLevel).orElse(0);

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
            if (prediction < data.getBukkitPlayerFromUUID().getWalkSpeed() + 0.02 * (speed + 1))
                prediction = data.getBukkitPlayerFromUUID().getWalkSpeed() + 0.02 * (speed + 1);

            // very lazy patch for a false flag
            if (ground > 1) {
                this.lastFriction = this.friction;
                this.friction = getBlockFriction(data) * 0.91f;
            }

            if (friction < lastFriction)
                prediction += landMovementFactor * 1.25;

            debug("pred=" + prediction + " deltaXZ=" + deltaXZ);

            //FIXME : EXEMPT FROM VELOCITY

            final boolean exempt =
                            new WorldUtils().isOnACertainBlock(data.getPlayer(), "ice")
                            || movementProcessor.isTeleported()
                            || new WorldUtils().isOnACertainBlock(data.getPlayer(), "door")
                            || new WorldUtils().isOnACertainBlock(data.getPlayer(), "slime");

            // flag
            if (deltaXZ > prediction && !exempt) {
                if (++this.buffer > 3)
                    fail("limit=" + prediction + " delta=" + deltaXZ);
            } else if (this.buffer > 0) buffer -= 0.025D;
        }
    }


    public float getBlockFriction(PlayerData data) {
        String block = data.getBukkitPlayerFromUUID().getLocation().add(0, -1, 0).getBlock().getType().name().toLowerCase();
        return block.equals("blue ice") ? 0.989f : block.contains("ice") ? 0.98f : block.equals("slime") ? 0.8f : 0.6f;
    }
}
