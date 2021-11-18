package arctic.ac.check.checks.movement.motion;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.WorldUtils;
import org.bukkit.entity.Player;

public class MotionG extends Check {

    private double lastDeltaY;
    private int airTicks;

    public MotionG(PlayerData data) {
        super(data, "Motion", "G", "movement.motion.g", "Checks for proper y axis accel.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {
            final MoveEvent event = (MoveEvent) e;

            final double deltaY = event.getDeltaY();
            final double lastDeltaY = this.lastDeltaY;

            this.lastDeltaY = deltaY;

            final double accelY = Math.abs(deltaY - lastDeltaY);

            final Player player = data.getPlayer();
            final WorldUtils worldUtils = new WorldUtils();


            if (event.isGround())
                this.airTicks = 0;
            else this.airTicks++;

            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isInLiquidVertically(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || worldUtils.isOnACertainBlock(player, "stairs")
                    || worldUtils.isOnACertainBlock(player, "ice")
                    || data.getInteractData().isHurt()
                    || worldUtils.haveABlockNearHead(player)
                    || worldUtils.isCloseToGround(player.getLocation());


            if (accelY < 1.0E-5D && !exempt && airTicks > 6) {
                if (++buffer > 2)
                    fail("accel=" + accelY);
            } else if (buffer > 0) buffer -= 0.025D;
        }

    }
}
