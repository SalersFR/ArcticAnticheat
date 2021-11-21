package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;

public class AimC extends Check {

    private float lastDeltaYaw;

    public AimC(PlayerData data) {
        super(data, "Aim", "C", "combat.aim.c", "Checks for too straight rotations.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {

            final RotationEvent event = (RotationEvent) e;

            final float deltaYaw = event.getDeltaYaw();
            final float deltaPitch = event.getDeltaPitch();

            final float lastDeltaYaw = this.lastDeltaYaw;
            this.lastDeltaYaw = deltaYaw;

            final double yawAccel = Math.abs(deltaYaw - lastDeltaYaw);
            final float pitch = event.getTo().getPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || deltaYaw < 1.2D;

            debug("deltaPitch=" + deltaPitch + " yawAccel=" + yawAccel + " buffer=" + buffer + " exempt=" + exempt);

            if ((yawAccel > 25.01D || (deltaYaw > 30.f && lastDeltaYaw > 15.f)) && Math.abs(deltaPitch) < 0.0001 && !exempt) {
                if (++this.buffer > 7) {
                    fail("deltaPitch=" + deltaPitch);
                }

            } else if (this.buffer > 0) this.buffer -= 0.5D;


        }
    }
}
