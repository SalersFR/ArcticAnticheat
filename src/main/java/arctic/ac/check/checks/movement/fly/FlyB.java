package arctic.ac.check.checks.movement.fly;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.WorldUtils;
import org.bukkit.entity.Player;

public class FlyB extends Check {

    private int airTicks;

    public FlyB(PlayerData data) {
        super(data, "Fly", "B", "movement.fly.b", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;


            final WorldUtils worldUtils = new WorldUtils();
            final Player player = data.getPlayer();

            final double deltaY = event.getDeltaY();
            final double motionY = player.getVelocity().getY();

            final double resultY = deltaY - motionY;

            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || airTicks < 9
                    || player.getFallDistance() > 10.0F
                    || event.isGround();


            if (worldUtils.isCloseToGround(data.getBukkitPlayerFromUUID().getLocation())) {
                this.airTicks = 0;
            } else this.airTicks++;

            debug("deltaY=" + deltaY + " motionY=" + motionY + " resultY=" + resultY + " exempt=" + exempt);

            if (resultY > 1.0D && !exempt) {
                if (++buffer > 1) {
                    fail("resultY=" + resultY);
                }
            } else if (buffer > 0) buffer -= 0.05D;
        }
    }
}
