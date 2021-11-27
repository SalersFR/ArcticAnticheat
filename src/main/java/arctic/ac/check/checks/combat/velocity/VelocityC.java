package arctic.ac.check.checks.combat.velocity;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VelocityC extends Check {

    public VelocityC(PlayerData data) {
        super(data, "Velocity", "C", "combat.velocity.c", "Checks for horizontal knockback modifications", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {
            final MoveEvent event = (MoveEvent) e;

            final double deltaXZ = event.getDeltaXZ();
            final WorldUtils worldUtils = new WorldUtils();

            final Player player = data.getPlayer();

            final boolean exempt = worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isInLiquid(player)
                    || worldUtils.isInLiquidVertically(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.haveABlockNearHead(player)
                    || worldUtils.blockNearHead(player.getLocation());

            int velocityTicks = data.getVelocityData().getVelocityTicks();

            if (velocityTicks <= 1 && !exempt && data.getVelocityData().getOriginalVelocityY() > 0.2) {
                double predictedVelocity = Math.sqrt((data.getVelocityData().getOriginalVelocityX() *
                        data.getVelocityData().getOriginalVelocityX()) + (data.getVelocityData().getOriginalVelocityZ() *
                        data.getVelocityData().getOriginalVelocityZ()));

                double ratio = deltaXZ / (predictedVelocity - 0.13);

                if (Math.abs(ratio) < 0.98F) {
                    buffer++;
                    if (buffer > 3.5) {
                        fail("velocity&f: " + ratio + "%" + " &9buffer&f: " + buffer);
                    }
                } else buffer -= 0.5;
            }
        }
    }
}
