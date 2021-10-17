package arctic.ac.check.checks.movement.motion;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.event.client.RotationEvent;

public class MotionE extends Check {

    private double deltaXZ, lastDeltaXZ;

    public MotionE(PlayerData data) {
        super(data, "Motion", "E", "movement.motion.e", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof MoveEvent) {

            final MoveEvent event = (MoveEvent) e;

            this.lastDeltaXZ = deltaXZ;
            this.deltaXZ = event.getDeltaXZ();

        } else if(e instanceof RotationEvent) {

            final RotationEvent event = (RotationEvent) e;

            final double deltaYaw = event.getDeltaYaw();
            final double accelXZ = Math.abs(deltaXZ - lastDeltaXZ);

            final double scaledAccel = accelXZ * 100;

            debug("deltaYaw=" + deltaYaw + " accelXZ=" + accelXZ + " current=" + deltaXZ  + " last=" + lastDeltaXZ);

            if(deltaYaw > 1.6F && deltaXZ > 0.15D && scaledAccel < 0.00001) {
                if(++buffer > 3)
                    fail("a=" + scaledAccel);
            } else if(buffer > 0) buffer -= 0.025D;

        }
    }
}
