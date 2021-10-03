package arctic.ac.check.checks.movement.motion;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.PlayerUtils;
import arctic.ac.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class MotionC extends Check {


    public MotionC(PlayerData data) {
        super(data, "Motion", "C", "movement.motion.c", false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {
            final MoveEvent event = (MoveEvent) e;

            final World world = data.getBukkitPlayerFromUUID().getWorld();

            final WorldUtils worldUtils = new WorldUtils();
            final double deltaY = event.getDeltaY();

            final Location bukkitTo = event.getTo().toVector().toLocation(world);
            final Location bukkitFrom = event.getFrom().toVector().toLocation(world);

            final Player player = data.getPlayer();

            final boolean jumped = worldUtils.isOnGround(bukkitFrom, -0.00001) && !worldUtils.isOnGround(bukkitTo, -0.00001) && deltaY > 0;

            final double deltaXZ = event.getDeltaXZ();

            final boolean exempt = worldUtils.blockNearHead(bukkitTo) || worldUtils.isCollidingWithClimbable(player)
                    || data.getInteractData().isTeleported() || worldUtils.isOnACertainBlock(player,"ice")||
                    data.getInteractionData().getTicksSinceHurt() < 40 || worldUtils.blockNearHead(bukkitFrom, player);


            final double limit = 0.6239;
            final double fixedLimit = player.hasPotionEffect(PotionEffectType.SPEED) ? limit +
                    (PlayerUtils.getPotionLevel(player, PotionEffectType.SPEED) * 0.11F) : limit;

            debug("jumped=" + jumped + " deltaY=" + deltaY + " dXZ=" + deltaXZ + "fixed=" + fixedLimit);

            if (deltaXZ > fixedLimit && jumped && !exempt) {
                if(++buffer > 2)
                    fail("moved too fast while jumping=" + deltaXZ);


            } else if(buffer > 0) buffer -= 0.001D;

        }
    }
}

