package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.utils.MathUtils;

public class AimO extends Check {

    private float lastDeltaPitch;

    public AimO(PlayerData data) {
        super(data, "Aim", "O", "combat.aim.o", "Checks for invalid very small GCD.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {
            final RotationEvent event = (RotationEvent) e;

            float deltaPitch = event.getDeltaPitch();
            float lastDeltaPitch = this.lastDeltaPitch;
            this.lastDeltaPitch = deltaPitch;

            final float gcd = (float) MathUtils.getGcd(Math.abs(deltaPitch), Math.abs(lastDeltaPitch));

            if (gcd < 0.01 && gcd != 0 && (event.getDeltaPitch() < 15 && event.getDeltaYaw() < 15) && data.getCinematicProcessor().getTicksSince() > 3) {
                if (++buffer > 10) {
                    fail("GCD=" + gcd);
                }
            } else if (buffer > 0) buffer -= 0.5;
        }
    }
}
