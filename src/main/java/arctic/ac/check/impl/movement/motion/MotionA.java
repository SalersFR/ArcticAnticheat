package arctic.ac.check.impl.movement.motion;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.CollisionProcessor;
import arctic.ac.data.processor.impl.MovementProcessor;
import arctic.ac.utils.PlayerUtils;
import eu.salers.salty.packet.type.PacketType;
import org.bukkit.potion.PotionEffectType;

public class MotionA extends Check {

    public MotionA(PlayerData data) {
        super(data, "Motion", "A", "movement.motion.a", "Checks for an invalid jump height.", true);
    }

    @Override
    public void handle(Object packet, PacketType packetType) {
        if (packetType == PacketType.IN_POSITION_LOOK || packetType == PacketType.IN_POSITION) {

            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final double deltaY = movementProcessor.getDeltaY();
            final boolean jumped = deltaY > 0 && collisionProcessor.getClientAirTicks() == 1;

            final double predicted = 0.42F;
            final double fixedPrediction = data.getPlayer().hasPotionEffect(PotionEffectType.JUMP)
                    ? predicted + PlayerUtils.getPotionLevel(data.getPlayer(), PotionEffectType.JUMP) : predicted;

            final boolean exempt = collisionProcessor.isBlockNearHead() || collisionProcessor.getSlimeTicks() <= 35 ||
                    Math.abs(0.01250003768371582 - deltaY) < 0.000001 || collisionProcessor.isClimbable() || collisionProcessor.getIceTicks() <= 12;


            if(jumped && !exempt && Math.abs(deltaY - fixedPrediction) > 1.0E-5D) {
                if(++buffer > 0) //yeah a buffer, funkemunky best
                    fail("pred=" + fixedPrediction + " delta=" + deltaY);

            } else if(buffer > 0) buffer -= 0.0025D;
        }

    }
}
