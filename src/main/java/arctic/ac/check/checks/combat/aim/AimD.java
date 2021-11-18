package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;

public class AimD extends Check {

    private float lastDeltaPitch, lastDeltaYaw;

    public AimD(PlayerData data) {
        super(data, "Aim", "D", "combat.aim.d", "Checks for repetitive rotations.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {
            RotationEvent event = (RotationEvent) e;

            float deltaYaw = event.getDeltaYaw();
            float lastDeltaYaw = this.lastDeltaYaw;

            float deltaPitch = event.getDeltaPitch();
            float lastDeltaPitch = this.lastDeltaPitch;

            this.lastDeltaPitch = deltaPitch;
            this.lastDeltaYaw = deltaYaw;

            debug("deltaPitch=" + deltaPitch + " lastDeltaPitch=" + lastDeltaPitch + " deltaYaw=" + deltaYaw + " lastDeltaYaw=" + lastDeltaYaw);

            if ((deltaYaw == lastDeltaYaw && deltaPitch > 0.4) || (deltaPitch == lastDeltaPitch && deltaPitch > 0.4)) {
                if (++buffer > 7) {
                    buffer -= 3.5D;
                    fail("deltaPitch=" + deltaPitch + " lastDeltaPitch=" + lastDeltaPitch + " deltaYaw=" + deltaYaw + " lastDeltaYaw=" + lastDeltaYaw);
                }
            } else if (buffer > 0) buffer--;

        }
    }
}
