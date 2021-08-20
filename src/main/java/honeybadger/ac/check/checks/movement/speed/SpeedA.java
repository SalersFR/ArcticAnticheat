package honeybadger.ac.check.checks.movement.speed;

import honeybadger.ac.check.Check;
import honeybadger.ac.data.PlayerData;
import honeybadger.ac.event.Event;
import honeybadger.ac.event.client.MoveEvent;
import honeybadger.ac.utils.PlayerUtils;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.NumberConversions;

public class SpeedA extends Check {

    private boolean wasOnGround;
    private int air, ground;
    private double lastDeltaXZ, lastFriction, friction;

    public SpeedA(PlayerData data) {
        super(data, "Speed", "A", "movement.speed.a", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {


            final MoveEvent event = (MoveEvent) e;

            final EntityPlayer player = ((CraftPlayer) data.getPlayer()).getHandle();

            final boolean onGround = data.getPlayer().isOnGround();

            air = onGround ? 0 : Math.min(air + 1, 20);
            ground = onGround ? Math.min(ground + 1, 20) : 0;

            // deltas
            final double deltaXZ = event.getDeltaXZ();
            final double lastDeltaXZ = this.lastDeltaXZ;
            this.lastDeltaXZ = deltaXZ;

            // Roll Back
            // landMovementFactor
            final float speed = PlayerUtils.getPotionLevel(data.getPlayer(), PotionEffectType.SPEED);
            final float slow = PlayerUtils.getPotionLevel(data.getPlayer(), PotionEffectType.SLOW);
            double d = 0.10000000149011612;
            d += d * 0.20000000298023224 * speed;
            d += d * -0.15000000596046448 * slow;

            // Sprint desync big gay just assume they are sprinting
            d += d * 0.30000001192092896;

            final float landMovementFactor = (float) d;

            // the check itself
            double prediction;
            if (ground > 2) {
                prediction = lastDeltaXZ * 0.91f * getFriction(player, data.getPlayer().getLocation()) + landMovementFactor;
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
                this.friction = getFriction(player, data.getPlayer().getLocation()) * 0.91f;
            }

            if (friction < lastFriction)
                prediction += landMovementFactor * 1.25;


            // flag
            if (deltaXZ > prediction) {
                if (this.vl++ > 3)
                    fail("deltaXZ=" + deltaXZ + " max=" + prediction);
            } else this.buffer -= this.buffer > 0 ? 0.025 : 0;

        }
    }

    private float getFriction(EntityPlayer player, Location location) {
        return player.world.getType(
                new BlockPosition(location.getX(),
                        NumberConversions.floor(location.getY()) - 1,
                        location.getZ())
        ).getBlock().frictionFactor;
    }

}
