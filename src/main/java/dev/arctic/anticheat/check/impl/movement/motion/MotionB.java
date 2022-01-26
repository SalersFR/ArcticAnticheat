package dev.arctic.anticheat.check.impl.movement.motion;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.utilities.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class MotionB extends Check {

    public MotionB(PlayerData data) {
        super(data, "Motion", "B", "movement.motion.b", "Checks for too fast air movement.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {
            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            final double deltaXZ = movementProcessor.getDeltaXZ();
            final boolean jumped = movementProcessor.getDeltaY() > 0 && collisionProcessor.getClientAirTicks() == 1;

            final boolean slime = collisionProcessor.isOnSlime() || collisionProcessor.isLastOnSlime() || collisionProcessor.isLastOnGroundSlime();
            final boolean exempt = collisionProcessor.isBonkingHead() || Math.abs(0.01250003768371582 - movementProcessor.
                    getDeltaY()) < 0.000001 || slime || collisionProcessor.isOnClimbable() ||
                    collisionProcessor.isOnIce() || collisionProcessor.isLastOnIce() || collisionProcessor.getFenceCollisions()
                    .stream().anyMatch(block -> block.isFence() || block.isFenceGate() || block.isWall() || block.isDoor()) ||
                    data.getVelocityProcessor().getVelocityTicks() <= 20;
            ;

            final Player player = data.getPlayer();

            final double limit = 0.6239;
            final double fixedLimit = player.hasPotionEffect(PotionEffectType.SPEED) ? limit +
                    (PlayerUtils.getPotionLevel(player, PotionEffectType.SPEED) * 0.11F) : limit;

            debug("jumped=" + jumped + " dXZ=" + deltaXZ + "fixed=" + fixedLimit);

            if (deltaXZ > (fixedLimit + (player.getWalkSpeed() * 0.33D)) && jumped && !exempt) {
                if (++buffer > 4)
                    fail("moved too fast while jumping=" + deltaXZ);


            } else if (buffer > 0) buffer -= 0.0125D;

            if (deltaXZ > (fixedLimit - 0.175D) && collisionProcessor.getClientAirTicks() == 0 && !exempt) {
                if (++buffer > 3.25)
                    fail("moved too fast without jumping=" + deltaXZ);


            } else if (buffer > 0) buffer -= 0.125D;


        }
    }
}
