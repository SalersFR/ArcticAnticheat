package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;

public class AimE extends Check {

    private double lastDeltaYaw;

    public AimE(PlayerData data) {
        super(data, "Aim", "E", "combat.aim.e","Checks for too smooth rotations (yaw)", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {

            final RotationEvent event = (RotationEvent) e;
            final double deltaYaw = event.getDeltaYaw();

            final double lastDeltaYaw = this.lastDeltaYaw;
            this.lastDeltaYaw = deltaYaw;

            final double accelYaw = Math.abs(deltaYaw - lastDeltaYaw);
            final double pitch = event.getTo().getPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || deltaYaw < 3.5D;

            debug("accelYaw=" + accelYaw + " deltaYaw=" + deltaYaw + " lastDeltaYaw=" + lastDeltaYaw + " exempt=" + exempt);

            if (exempt) return;

            if (accelYaw <= 0.01) {
                if (++buffer > 3) {
                    fail("accel=" + accelYaw);
                }
            } else if (buffer > 0) buffer -= 0.1D;


        }

    }
}
