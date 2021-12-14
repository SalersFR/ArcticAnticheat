package arctic.ac.check.impl.movement.motion;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.CollisionProcessor;
import arctic.ac.data.processor.impl.MovementProcessor;
import arctic.ac.utils.PlayerUtils;
import arctic.ac.utils.WorldUtils;
import eu.salers.salty.packet.type.PacketType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class MotionD extends Check {

    public MotionD(PlayerData data) {
        super(data, "Motion", "D", "movement.motion.d", "Checks for invalid y movement near ground.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType) {
        if (packetType == PacketType.IN_POSITION_LOOK || packetType == PacketType.IN_POSITION) {

            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final WorldUtils worldUtils = new WorldUtils();
            final Player player = data.getPlayer();

            final double deltaY = movementProcessor.getDeltaY();
            final double lastDeltaY = movementProcessor.getLastDeltaY();

            final boolean exempt = collisionProcessor.isLiquid() || collisionProcessor.isNearBoat() ||
                    collisionProcessor.isBlockNearHead()|| worldUtils.isOnACertainBlock(player, "stairs") ||
                    worldUtils.isOnACertainBlock(player, "ice") || collisionProcessor.isWeb();

            //FIXME : EXEMPT FROM VELOCITY

            if (deltaY < 0 && Math.abs(deltaY) == lastDeltaY && lastDeltaY > 0.2F
                    && collisionProcessor.getCollisionGroundTicks() >= 1 && !exempt) {
                if (++buffer > 0)
                    fail("same pos/neg motion\ndelta=" + deltaY + " last=" + lastDeltaY);

            } else if (buffer > 0) buffer -= 0.0025D;

            if (deltaY <= 0.42f && lastDeltaY >= 0.42f && collisionProcessor.getCollisionGroundTicks() >= 1 && !exempt) {
                if (++buffer > 0)
                    fail("jump (or higher) reversed motion\ndelta=" + deltaY + " last=" + lastDeltaY);

            } else if (buffer > 0) buffer -= 0.0025D;

        }


    }
}
