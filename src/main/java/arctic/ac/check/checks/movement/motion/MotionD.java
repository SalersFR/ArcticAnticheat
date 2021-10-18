package arctic.ac.check.checks.movement.motion;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.WorldUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class MotionD extends Check {

    private int ticksSinceJumped;
    private int airTicks;


    public MotionD(PlayerData data) {
        super(data, "Motion", "D", "movement.motion.d", "Checks for fast falls modules.",false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;
            final WorldUtils worldUtils = new WorldUtils();
            final Player player = data.getPlayer();
            final World world = player.getWorld();

            final double deltaY = event.getDeltaY();

            final Location bukkitTo = event.getTo().toVector().toLocation(world);
            final Location bukkitFrom = event.getFrom().toVector().toLocation(world);

            final boolean exempt = worldUtils.isInLiquid(player) || worldUtils.isCollidingWithClimbable(player) || worldUtils.isCollidingWithWeb(player);

            final boolean jumped = worldUtils.isOnGround(bukkitFrom, -0.00001) &&
                    !worldUtils.isOnGround(bukkitTo, -0.00001) && deltaY > 0;

            if(event.isGround())
                this.airTicks = 0;

            if (jumped)
                ticksSinceJumped = 0;

            debug("deltaY=" + deltaY + " ticksSinceJumped=" + ticksSinceJumped + " airTicks=" + airTicks);

            if (ticksSinceJumped <= 10 && deltaY < -0.38 && !exempt) {
                if (++buffer > 2)
                    fail("fast fall jump\ndDY:" + deltaY);

            } else if (buffer > 0) buffer -= 0.01D;

            if (airTicks > 22 && deltaY < -3.79D && !exempt ) {
                if (++buffer > 2)
                    fail("fast fall \ndDY:" + deltaY);

            } else if (buffer > 0) buffer -= 0.01D;

            if(deltaY < -4.3275 && !exempt && airTicks > 0) {
                if (++buffer > 2)
                    fail("very fast fall \ndDY:" + deltaY);
            }


            this.ticksSinceJumped++;
            this.airTicks++;

        }

    }
}
