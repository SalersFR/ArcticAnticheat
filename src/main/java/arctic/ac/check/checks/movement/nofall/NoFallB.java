package arctic.ac.check.checks.movement.nofall;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.WorldUtils;
import org.bukkit.entity.Player;

public class NoFallB extends Check {

    private int airTicks,ticksEdge;

    public NoFallB(PlayerData data) {
        super(data, "NoFall", "B", "movement.nofall.b", false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;

            final boolean packetGround = event.isGround();

            final Player player = data.getPlayer();
            final WorldUtils worldUtils = new WorldUtils();

            if(worldUtils.isAtEdgeOfABlock(player))  {
                this.ticksEdge = 0;
            }else this.ticksEdge++;

            if (worldUtils.isCloseToGround(data.getBukkitPlayerFromUUID().getLocation())) {
                this.airTicks = 0;
            } else this.airTicks++;

            final boolean serverGround = airTicks < 21;



            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || worldUtils.isOnACertainBlock(player, "fence")
                    || ticksEdge < 20;

            if (!serverGround && packetGround && !exempt) {
                fail("airTicks=" + airTicks);
            }


        }
    }
}
