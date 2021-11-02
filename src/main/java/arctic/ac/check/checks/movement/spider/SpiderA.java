package arctic.ac.check.checks.movement.spider;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.MathUtils;
import arctic.ac.utils.PlayerUtils;
import arctic.ac.utils.WorldUtils;
import org.bukkit.World;

public class SpiderA extends Check {

    private double deltaY, lastDeltaY, lastLastDeltaY;

    public SpiderA(PlayerData data) {
        super(data, "Spider", "A", "movement.spider.a", "Checks for matching deltaY values when on walls.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {
            MoveEvent event = (MoveEvent) e;

            double deltaY = Math.abs(event.getDeltaY());
            this.deltaY = deltaY;
            double lastDeltaY = this.deltaY;
            this.deltaY = deltaY;
            double lastLastDeltaY = this.lastDeltaY;
            this.lastDeltaY = lastDeltaY;
            double lastLastLastDeltaY = this.lastLastDeltaY;
            this.lastLastDeltaY = lastLastDeltaY;

            boolean exempt = data.getInteractionData().isHurt()
                    || data.getInteractionData().isTeleported()
                    || new WorldUtils().isCollidingWithClimbable(data.getPlayer())
                    || new WorldUtils().isCollidingWithWeb(data.getPlayer())
                    || new WorldUtils().isCloseToGround(data.getPlayer().getLocation())
                    || new WorldUtils().isOnACertainBlock(data.getPlayer(), "stairs");


            debug("dY=" + deltaY + " LdY=" + lastDeltaY + " LLdY=" + lastLastDeltaY + " LLLdY=" + lastLastLastDeltaY);
            if (MathUtils.areAllEqual(deltaY, lastDeltaY, lastLastDeltaY, lastLastLastDeltaY) && !exempt && deltaY > 0) {
                if (++buffer > 5.0) {
                    fail("allDY=" + deltaY);
                }
            } else if (buffer > 0) buffer -= 0.1;
        }
    }
}
