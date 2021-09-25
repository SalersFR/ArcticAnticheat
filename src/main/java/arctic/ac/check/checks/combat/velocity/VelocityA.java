package arctic.ac.check.checks.combat.velocity;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.WorldUtils;
import org.bukkit.entity.Player;

public class VelocityA extends Check {

    public VelocityA(PlayerData data) {
        super(data, "Velocity", "A", "combat.velocity.a", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {
            final MoveEvent event = (MoveEvent) e;

            final double deltaY = event.getDeltaY();
            final WorldUtils worldUtils = new WorldUtils();

            final Player player = data.getPlayer();

            final boolean exempt = worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isInLiquid(player)
                    || worldUtils.isInLiquidVertically(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.haveABlockNearHead(player);

            int velocityTicks = data.getVelocityData().getVelocityTicks() - 1;

            if(velocityTicks < 5 && !exempt) {
                double predictedVelocity = data.getVelocityData().getVelocityY();

                double ratio = deltaY / predictedVelocity;

                if(ratio > 0 && ratio < 0.9999) {
                    if(buffer++ > 0) {
                        fail("percentage="+(float)ratio+" deltaY="+(float)deltaY+" predicted="+(float)predictedVelocity);
                        buffer = 0;
                    }
                } else buffer = Math.max(0, buffer - 0.1);

                debug("percentage="+(float)(ratio*100.0)+", deltaY="+(float)deltaY+", predicted="+(float)predictedVelocity);
            }
        }
    }
}
