package dev.arctic.anticheat.check.impl.movement.motion;

import dev.arctic.anticheat.check.Check;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.data.processors.impl.CollisionProcessor;
import dev.arctic.anticheat.data.processors.impl.MovementProcessor;
import dev.arctic.anticheat.packet.Packet;
import org.bukkit.util.Vector;

public class MotionF extends Check {

    private double motionY = 0.0D;

    public MotionF(PlayerData data) {
        super(data, "Motion", "F", "movement.motion.f", "Checks for invalid water movement.", true);
    }

    @Override
    public void handle(Packet packet, long time) {
        if (packet.isFlying()) {

            final CollisionProcessor collisionProcessor = data.getCollisionProcessor();
            final MovementProcessor movementProcessor = data.getMovementProcessor();

            final double deltaY = movementProcessor.getDeltaY();


            if (collisionProcessor.getWaterTicks() >= 15 && !collisionProcessor.isCollisionOnGround()) {

                //jump handling
                if (deltaY > 0)
                    this.motionY += 0.05225599999910593033D;


                this.motionY *= 0.800000011920929D;
                this.motionY -= 0.02D;

                final Vector realMove = new Vector(0, Math.abs(deltaY), 0);
                final Vector predMove = new Vector(0, Math.abs(motionY), 0);

                final double offset = realMove.distance(predMove);

                if(offset >= 0.1 && Math.abs(motionY) >= 0.055) {
                    if(++buffer > 4) {
                        fail("offset=" + offset);

                    }
                } else if(buffer > 0) buffer -= 0.125D;

                debug("mot=" + motionY + " delta=" + deltaY + " offset=" + offset);


            }


        }
    }


}
