package polar.ac.check.checks.motion;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.MoveEvent;
import polar.ac.utils.PlayerUtils;
import polar.ac.utils.WorldUtils;

public class MotionA extends Check {

    public MotionA(PlayerData data) {
        super(data, "Motion","A","movement.motion.a",false);
    }

    @Override
    public void handle(Event e) {
        if(e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;

            final double deltaY = event.getDeltaY();

            double jumpLimit = 0.4199999;

            final Player player = data.getPlayer();

            if(player.hasPotionEffect(PotionEffectType.JUMP)) {
                jumpLimit += (PlayerUtils.getPotionLevel(player,PotionEffectType.JUMP)) * 0.1F;
            }

            final WorldUtils worldUtils = new WorldUtils();

            final boolean exempt = worldUtils.isInLiquid(player)
                    || worldUtils.isCollidingWithClimbable(player)
                    || worldUtils.isNearBoat(player)
                    || worldUtils.isCollidingWithWeb(player)
                    || worldUtils.isAtEdgeOfABlock(player)
                    || player.getFallDistance() > 10.0F
                    || event.isGround()
                    || data.getInteractionData().getTicksSinceSlime() < 120;

            debug("exempt="+ exempt + " limit=" + jumpLimit + " deltaY=" + deltaY);

            if(deltaY > jumpLimit && !exempt) {
                if(++buffer > 1) {
                    fail("deltaY=" + deltaY);
                }
            }else if(buffer > 0) buffer *= 0.99D;


        }
    }
}
