package arctic.ac.check.checks.movement.fly;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.BlockPlaceEvent;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.WorldUtils;
import org.bukkit.entity.Player;

public class FlyA extends Check {

    private double airTicks, ticksPlace;

    public FlyA(PlayerData data) {
        super(data, "Fly", "A", "movement.fly.a", "Checks for invalid motion y.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;


            if (event.isGround())
                this.airTicks = 0;
            else this.airTicks++;

            final Player player = data.getPlayer();
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
                    || worldUtils.haveABlockNearHead(player)
                    || data.getVelocityData().getVelocityTicks() <= 7;

            if (data.getPlayer().getVelocity().getY() == -0.078400001525878 && !exempt && airTicks >= 21) {
                if (++buffer > 3)
                    fail("mY=0.078400001525878");
            } else if (buffer > 0) buffer -= 0.2D;


            this.ticksPlace++;

        } else if (e instanceof BlockPlaceEvent) {
            this.ticksPlace = 0;

        }
    }


}
