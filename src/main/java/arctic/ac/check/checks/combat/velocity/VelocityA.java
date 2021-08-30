package arctic.ac.check.checks.combat.velocity;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.event.server.ServerVelocityEvent;
import arctic.ac.utils.WorldUtils;
import org.bukkit.entity.Player;

public class VelocityA extends Check {

    private double lastVelY;
    private int ticksSinceVel;

    public VelocityA(PlayerData data) {
        super(data, "Velocity", "A", "combat.velocity.a", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof ServerVelocityEvent) {

            final ServerVelocityEvent event = (ServerVelocityEvent) e;

            this.ticksSinceVel = 0;

            this.lastVelY = event.getY();
        } else if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;

            final double deltaY = event.getDeltaY();
            final WorldUtils worldUtils = new WorldUtils();

            final Player player = data.getPlayer();

            final int maxTicksForResponse = ticksSinceVel + 6;

            final boolean exempt = worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isInLiquid(player)
                    || worldUtils.isInLiquidVertically(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.haveABlockNearHead(player);

            if (lastVelY > 0.0 && !exempt && data.getInteractData().isHurt()) {
                if (ticksSinceVel <= maxTicksForResponse
                        && deltaY < lastVelY) {
                    if (++buffer > maxTicksForResponse) {
                        buffer = 0;
                        fail("deltaY=" + deltaY + " velY=" + lastVelY);
                    }

                }


            }
        } else if (e instanceof FlyingEvent) {
            this.ticksSinceVel++;
        }
    }
}
