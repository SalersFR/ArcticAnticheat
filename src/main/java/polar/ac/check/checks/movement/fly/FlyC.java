package polar.ac.check.checks.movement.fly;

import org.bukkit.entity.Player;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.MoveEvent;
import polar.ac.utils.WorldUtils;

public class FlyC extends Check {

    private double lastDeltaY, airTicks;

    public FlyC(PlayerData data) {
        super(data,"Fly","C","movement.fly.c",true);
    }


    @Override
    public void handle(Event e) {
        if(e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;


            final double deltaY = event.getDeltaY();
            final double lastDeltaY = this.lastDeltaY;

            this.lastDeltaY = deltaY;

            final WorldUtils worldUtils = new WorldUtils();

            if (worldUtils.isCloseToGround(data.getBukkitPlayerFromUUID().getLocation())) {
                this.airTicks = 0;
            } else this.airTicks++;



            final Player player = data.getBukkitPlayerFromUUID();

            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || airTicks < 9
                    || player.getFallDistance() > 5.0F
                    || event.isGround();

            if(!exempt && deltaY > lastDeltaY) {
                if(++buffer > 2.0D) {
                    fail("deltaY=" + deltaY + " lastDeltaY=" + lastDeltaY);
                }
            } else if(buffer > 0) buffer -= 0.1D;



        }
    }
}
