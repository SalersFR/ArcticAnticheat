package polar.ac.check.checks.movement.nofall;

import org.bukkit.entity.Player;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.MoveEvent;
import polar.ac.utils.WorldUtils;

public class NoFallB extends Check {

    private int airTicks;

    public NoFallB(PlayerData data) {
        super(data, "NoFall","B","movement.nofall.b",false);
    }

    @Override
    public void handle(Event e) {
        if(e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;

            final boolean packetGround = event.isGround();

            final WorldUtils worldUtils = new WorldUtils();

            if (worldUtils.isCloseToGround(data.getBukkitPlayerFromUUID().getLocation())) {
                this.airTicks = 0;
            } else this.airTicks++;

            final boolean serverGround = airTicks < 21;

            final Player player = data.getPlayer();

            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player);

            if(!serverGround && packetGround && !exempt)  {
                fail("airTicks=" + airTicks);
            }


        }
    }
}
