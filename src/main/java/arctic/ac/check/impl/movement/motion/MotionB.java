package arctic.ac.check.impl.movement.motion;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.CollisionProcessor;
import arctic.ac.data.processor.impl.MovementProcessor;
import arctic.ac.utils.PlayerUtils;
import eu.salers.salty.packet.type.PacketType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class MotionB extends Check {

    public MotionB(PlayerData data) {
        super(data, "Motion", "B", "movement.motion.b", "Checks for too fast air movement.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_POSITION_LOOK || packetType == PacketType.IN_POSITION) {

            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final double deltaXZ = movementProcessor.getDeltaXZ();
            final boolean jumped = movementProcessor.getDeltaY() > 0 && collisionProcessor.getClientAirTicks() == 1;

            final boolean exempt = collisionProcessor.isBlockNearHead() || collisionProcessor.getSlimeTicks() <= 35 ||
                    Math.abs(0.01250003768371582 - movementProcessor.getDeltaY()) < 0.000001 ||
                    collisionProcessor.isClimbable() || collisionProcessor.getIceTicks() <= 12;

            final Player player = data.getPlayer();

            //FIXME : EXEMPT FROM VELOCITY

            final double limit = 0.6239;
            final double fixedLimit = player.hasPotionEffect(PotionEffectType.SPEED) ? limit +
                    (PlayerUtils.getPotionLevel(player, PotionEffectType.SPEED) * 0.11F) : limit;

            debug("jumped=" + jumped + " dXZ=" + deltaXZ + "fixed=" + fixedLimit);

            if (deltaXZ > (fixedLimit + (player.getWalkSpeed() * 0.33D)) && jumped && !exempt) {
                if (++buffer > 2)
                    fail("moved too fast while jumping=" + deltaXZ);


            } else if (buffer > 0) buffer -= 0.001D;



        }

    }
}
