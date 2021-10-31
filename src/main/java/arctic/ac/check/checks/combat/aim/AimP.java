package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.utils.MathUtils;

public class AimP extends Check {

    private float lastDeltaPitch;

    public AimP(PlayerData data) {
        super(data, "Aim", "P", "combat.aim.p", "Checks for a GCD bypassing method.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {
            final RotationEvent event = (RotationEvent) e;

            float deltaPitch = event.getDeltaPitch();
            float lastDeltaPitch = this.lastDeltaPitch;
            this.lastDeltaPitch = deltaPitch;

            final float gcd = (float) MathUtils.getGcd(Math.abs(deltaPitch), Math.abs(lastDeltaPitch));

            if (gcd == 0 && (event.getDeltaYaw() > 1.1 || event.getDeltaPitch() > 1.1)  && (event.getDeltaPitch() < 15 && event.getDeltaYaw() < 15)) {
                if (++buffer > 10) {
                    fail("GCD=" + gcd + ", dYaw=" + event.getDeltaYaw() + ", dPitch=" + deltaPitch);
                }
            } else if (buffer > 0) buffer -= 0.5;
        }
    }
}
