package arctic.ac.check.impl.movement.fly;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.impl.CollisionProcessor;
import arctic.ac.data.processor.impl.MovementProcessor;
import arctic.ac.utils.WorldUtils;
import eu.salers.salty.packet.type.PacketType;

public class FlyA extends Check {

    public FlyA(PlayerData data) {
        super(data, "Fly", "A", "movement.fly.a", "Checks if player is following normal fall motion.", false);
    }

    @Override
    public void handle(Object packet, PacketType packetType, long time) {
        if (packetType == PacketType.IN_POSITION_LOOK || packetType == PacketType.IN_POSITION) {

            final MovementProcessor movementProcessor = data.getMovementProcessor();
            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();

            double motionY = movementProcessor.getDeltaY();
            double lastMotionY = movementProcessor.getLastDeltaY();

            double predictedMotionY = (lastMotionY - 0.08D) * 0.9800000190734863D;

            final WorldUtils worldUtils = new WorldUtils();
            boolean onGroundV2 = worldUtils.isCloseToGround(data.getBukkitPlayerFromUUID().getLocation());

            if (data.getPlayer().getVelocity().getY() > -0.7) return;
            if (data.getPlayer().getAllowFlight()) return;
            if (movementProcessor.isTeleported()) return;

            if (Math.abs(predictedMotionY) <= 0.005) predictedMotionY = 0;

            debug("predicted=" + predictedMotionY + " delta=" + motionY);

            if (collisionProcessor.getClientAirTicks() >= 5 && !onGroundV2 && movementProcessor.getTeleportTicks() > 1) {
                if (!check(motionY, predictedMotionY)) {
                    buffer += 10;
                    if (buffer > 30) {
                        fail("motionY " + motionY + " predicted " + predictedMotionY);
                    }
                } else {
                    if (buffer > 0) buffer--;
                }
            }
        }

    }


    private boolean check(double d1, double d2) {
        return Math.abs(d1 - d2) < 0.001;
    }
}
