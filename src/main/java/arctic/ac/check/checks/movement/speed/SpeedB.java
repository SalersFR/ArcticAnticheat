package arctic.ac.check.checks.movement.speed;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.WorldUtils;
import org.bukkit.entity.Player;

public class SpeedB extends Check {

    private int gTicks, aTicks, ticksSinceAir;

    public SpeedB(PlayerData data) {
        super(data, "Speed", "B", "movement.speed.b", true);
    }


    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent moveEvent = (MoveEvent) e;

            final double deltaXZ = moveEvent.getDeltaXZ();

            if (deltaXZ > 1.7D) {
                fail("very blatant speed=" + deltaXZ);
                return;
            }

            double limit = 0.7D;

            final boolean ground = moveEvent.isGround();

            final WorldUtils worldUtils = new WorldUtils();
            final Player player = data.getPlayer();

            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || worldUtils.isOnACertainBlock(player, "ICE")
                    || worldUtils.isOnACertainBlock(player, "FENCE")
                    || data.getInteractData().getTicksSinceHurt() < 140;

            if (exempt) return;

            if (ground) {
                gTicks++;
                aTicks = 0;
            } else {
                gTicks = 0;
                aTicks++;
            }

            if (gTicks > 10 || ground) {
                limit = 0.42873899;


            } else {
                limit = 0.689;
                this.ticksSinceAir = 0;
            }

            if (deltaXZ > limit && !exempt) {
                if (++buffer > 2) {
                    fail(deltaXZ + ">=" + limit);
                }
            } else if (buffer > 0) buffer *= 0.99D;


            this.ticksSinceAir++;
        }

    }
}
