package arctic.ac.check.checks.combat.velocity;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.impl.VelocityData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.MathUtils;
import arctic.ac.utils.WorldUtils;
import org.bukkit.entity.Player;

public class VelocityB extends Check {

    public VelocityB(PlayerData data) {
        super(data, "Velocity", "B", "combat.velocity.b", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;
            final VelocityData velocityData = data.getVelocityData();

            final WorldUtils worldUtils = new WorldUtils();

            final Player player = data.getPlayer();

            final double deltaXZ = event.getDeltaXZ();


            final boolean exempt = worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isInLiquid(player)
                    || worldUtils.isInLiquidVertically(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.haveABlockNearHead(player)
                    || worldUtils.blockNearHead(player.getLocation());

            int velocityTicks = data.getVelocityData().getVelocityTicks() - 1;


            if (velocityTicks < 5 && !exempt) {
                final double predictedVelocity = MathUtils.hypot(velocityData.getVelocityX(), velocityData.getVelocityZ());

                double ratio = deltaXZ / predictedVelocity;

                if (ratio > 0 && ratio < 0.9999) {
                    if (buffer++ > 3) {
                        // fail("percentage=" + (float) ratio + " deltaXZ=" + (float) deltaXZ + " predicted=" + (float) predictedVelocity);
                        buffer = 0;
                    }
                } else buffer = Math.max(0, buffer - 0.5);

                debug("percentage=" + (float) (ratio * 100.0) + ", deltaXZ=" + (float) deltaXZ + ", predicted=" + (float) predictedVelocity);
            }
        }
    }

}

