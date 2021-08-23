package polar.ac.check.checks.movement.fly;

import org.bukkit.entity.Player;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.MoveEvent;
import polar.ac.utils.WorldUtils;

public class FlyA extends Check {

    private double lastDeltaY, airTicks;

    public FlyA(PlayerData data) {
        super(data, "Fly", "A", "movement.fly.a", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;

            final double deltaY = event.getDeltaY();
            final double lastDeltaY = this.lastDeltaY;

            this.lastDeltaY = deltaY;

            final WorldUtils worldUtils = new WorldUtils();

            if (worldUtils.isCloseToGround(data.getBukkitPlayerFromUUID().getLocation())) {
                this.airTicks = 0;
            } else this.airTicks++;

            /*final*/
            double predictedDeltaY = (lastDeltaY - 0.08) * 0.98F;


            if (Math.abs(predictedDeltaY) < 0.005) {
                predictedDeltaY = 0;
            }

            final double result = Math.abs(deltaY - predictedDeltaY);


            final Player player = data.getBukkitPlayerFromUUID();

            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || airTicks < 9
                    || player.getFallDistance() > 10.0F
                    || event.isGround();

            debug("result=" + result + " exempt=" + exempt + " deltaY=" + deltaY + " lastDeltaY=" + lastDeltaY + " airTicks=" + airTicks);


            if (result > 0.01 && !exempt && !worldUtils.isCloseToGround(player.getLocation())) {
                if (++buffer > 3) {
                    fail("result=" + result);

                }

            } else if (buffer > 0) buffer -= 0.05D;


        }
    }


}
