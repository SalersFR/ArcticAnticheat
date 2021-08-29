package arctic.ac.check.checks.movement.motion;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.utils.PlayerUtils;
import arctic.ac.utils.WorldUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class MotionA extends Check {

    public MotionA(PlayerData data) {
        super(data, "Motion", "A", "movement.motion.a", false);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;

            final double deltaY = event.getDeltaY();

            double jumpLimit = 0.42F;

            final Player player = data.getPlayer();

            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                jumpLimit += (PlayerUtils.getPotionLevel(player, PotionEffectType.JUMP)) * 0.1F;
            }

            final WorldUtils worldUtils = new WorldUtils();

            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || player.getFallDistance() > 10.0F
                    || event.isGround()
                    || data.getInteractionData().getTicksSinceSlime() < 120
                    || data.getInteractionData().getTicksSinceSlime() < 120
                    || data.getInteractData().isHurt();

            debug("exempt=" + exempt + " limit=" + jumpLimit + " deltaY=" + deltaY);

            if (deltaY > jumpLimit && !exempt) {
                if (++buffer > 1) {
                    fail("deltaY=" + deltaY);
                }
            } else if (buffer > 0) buffer *= 0.99D;


        }
    }
}
