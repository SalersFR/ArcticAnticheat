package arctic.ac.check.checks.movement.motion;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.WorldUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MotionH extends Check {

    private double lastDeltaY;

    public MotionH(PlayerData data) {
        super(data, "Motion", "H", "movement.motion.h", "Checks for invalid y movement while being near ground.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {
            final MoveEvent event = (MoveEvent) e;

            final double deltaY = event.getDeltaY();
            final double lastDeltaY = this.lastDeltaY;

            this.lastDeltaY = deltaY;

            final Player player = data.getPlayer();

            final Location bukkitTo = event.getTo().toVector().toLocation(player.getWorld());
            final Location bukkitFrom = event.getFrom().toVector().toLocation(player.getWorld());

            final WorldUtils worldUtils = new WorldUtils();

            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isInLiquidVertically(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || worldUtils.isOnACertainBlock(player, "stairs")
                    || worldUtils.isOnACertainBlock(player, "ice")
                    || data.getInteractData().isHurt()
                    || worldUtils.haveABlockNearHead(player);


            if (deltaY < 0 && Math.abs(deltaY) == lastDeltaY && lastDeltaY > 0.2F && worldUtils.isCloseToGround(bukkitFrom) &&
                    worldUtils.isCloseToGround(bukkitTo) && !exempt) {
                if (++buffer > 0)
                    fail("same pos/neg motion\ndelta=" + deltaY + " last=" + lastDeltaY);

            } else if (buffer > 0) buffer -= 0.0025D;

            if (deltaY <= 0.42f && lastDeltaY >= 0.42f && worldUtils.isCloseToGround(bukkitFrom) &&
                    worldUtils.isCloseToGround(bukkitTo) && !exempt) {
                if (++buffer > 0)
                    fail("jump (or higher) reversed motion\ndelta=" + deltaY + " last=" + lastDeltaY);

            } else if (buffer > 0) buffer -= 0.0025D;

        }
    }

}

