package arctic.ac.check.checks.movement.motion;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.WorldUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MotionF extends Check {

    private double lastDeltaXZ;
    private int ticksSinceJump;

    public MotionF(PlayerData data) {
        super(data, "Motion", "F", "movement.motion.f", "Checks if player is following some mc rules.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;
            final WorldUtils worldUtils = new WorldUtils();

            final Player player = data.getPlayer();

            final Location bukkitTo = event.getTo().toVector().toLocation(player.getWorld());
            final Location bukkitFrom = event.getFrom().toVector().toLocation(player.getWorld());

            final double deltaXZ = event.getDeltaXZ();
            final double lastDeltaXZ = this.lastDeltaXZ;

            final double deltaY = event.getDeltaY();
            this.lastDeltaXZ = deltaXZ;

            final boolean jumped = worldUtils.isOnGround(bukkitFrom, -0.00001) && !worldUtils.isOnGround(bukkitTo, -0.00001) && deltaY > 0;

            if (jumped) ticksSinceJump = 0;

            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isInLiquidVertically(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || worldUtils.isOnACertainBlock(player, "stairs")
                    || worldUtils.isOnACertainBlock(player, "ice")
                    || data.getInteractData().isHurt()
                    || data.getInteractData().getTicksSinceHurt() < 20
                    || worldUtils.haveABlockNearHead(player);

            final double accelXZ = Math.abs(deltaXZ - lastDeltaXZ);

            if (!exempt && ticksSinceJump <= 2 && accelXZ <= 0.000001D && deltaXZ > 0.2D) {
                if (++buffer > 2)
                    fail("accel=" + accelXZ);
            } else if (buffer > 0) buffer -= 0.025D;

            this.ticksSinceJump++;


        }
    }

}
