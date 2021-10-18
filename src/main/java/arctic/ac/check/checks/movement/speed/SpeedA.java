package arctic.ac.check.checks.movement.speed;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.PlayerUtils;
import arctic.ac.utils.WorldUtils;
import org.bukkit.potion.PotionEffectType;

public class SpeedA extends Check {

    private boolean wasOnGround;
    private int air, ground;
    private double lastDeltaXZ, lastFriction, friction;

    public SpeedA(PlayerData data) {
        super(data, "Speed", "A", "movement.speed.a", "Checks if player is following minecraft's movement protocol.",true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {


            final MoveEvent event = (MoveEvent) e;

            boolean onGround = event.isGround();
            boolean wasOnGround = this.wasOnGround;
            this.wasOnGround = onGround;

            air = onGround ? 0 : Math.min(air + 1, 20);
            ground = onGround ? Math.min(ground + 1, 20) : 0;

            // deltas
            double deltaXZ = event.getDeltaXZ();
            double lastDeltaXZ = this.lastDeltaXZ;
            this.lastDeltaXZ = deltaXZ;

            // landMovementFactor
            float speed = PlayerUtils.getPotionLevel(data.getBukkitPlayerFromUUID(), PotionEffectType.SPEED);
            float slow = PlayerUtils.getPotionLevel(data.getBukkitPlayerFromUUID(), PotionEffectType.SLOW);
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

            if (data.getInteractData().getTicksSinceBow() < 11)
                prediction += 0.49D;

            final boolean exempt = data.getInteractionData().isHurt() ||
                    new WorldUtils().isOnACertainBlock(data.getPlayer(), "ice") || data.getInteractData().getTicksSinceHurt() < 140
                    || data.getInteractionData().isTeleported() ||
                    new WorldUtils().isOnACertainBlock(data.getPlayer(), "door");

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
