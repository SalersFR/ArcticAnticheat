package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;

public class AimJ extends Check {

    private double lastDeltaYaw, lastLastDeltaYaw;

    public AimJ(PlayerData data) {
        super(data, "Aim", "J", "combat.aim.j", true);
    }

    @Override
    public void handle(Event e) {

        if (e instanceof RotationEvent) {

            final RotationEvent event = (RotationEvent) e;
            final float deltaYaw = event.getDeltaYaw();

            final double lastDeltaYaw = this.lastDeltaYaw;
            this.lastDeltaYaw = deltaYaw;

            final double lastLastDeltaYaw = this.lastLastDeltaYaw;
            this.lastLastDeltaYaw = lastDeltaYaw;


            debug("current=" + deltaYaw + " last=" + lastDeltaYaw + " lastLast=" + lastLastDeltaYaw);
            final double pitch = event.getTo().getPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || deltaYaw < 3.5D;


            if (deltaYaw < 3.5F && lastDeltaYaw > 25.F && lastLastDeltaYaw < 5.0F && !exempt) {
                if (++buffer > 6) {
                    fail("lastDeltaYaw=" + lastDeltaYaw);
                }

            } else if (buffer > 0) buffer -= 0.25D;

        }
    }
}
