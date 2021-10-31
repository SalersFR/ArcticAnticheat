package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.utils.MathUtils;

public class AimQ extends Check {

    private float lastDeltaPitch;

    public AimQ(PlayerData data) {
        super(data, "Aim", "Q", "combat.aim.q", "Checks for a GCD fix method.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {
            final RotationEvent event = (RotationEvent) e;

            float deltaPitch = event.getDeltaPitch();
            float deltaYaw = event.getDeltaYaw();

            float lastDeltaPitch = this.lastDeltaPitch;
            this.lastDeltaPitch = event.getDeltaPitch();

            final float gcd = (float) MathUtils.getGcd(Math.abs(event.getDeltaPitch()), Math.abs(lastDeltaPitch));

            double resYaw = deltaYaw % gcd;
            double resPitch = deltaPitch % gcd;

            double modulusResPitch = MathUtils.getReversedModulus(gcd, deltaPitch, resPitch);
            double modulusResYaw = MathUtils.getReversedModulus(gcd, deltaYaw, resYaw);

            if ((Double.isNaN(modulusResPitch) || Double.isNaN(modulusResYaw)) && (deltaYaw >= 0.45 || deltaPitch >= 0.45)  && (event.getDeltaPitch() < 15 && event.getDeltaYaw() < 15)) {
                if (++buffer > 10.0) {
                    fail("modPitch: " + modulusResPitch + ", modYaw: " + modulusResYaw +
                            ", dYaw: " + deltaYaw + ", dPitch: " + deltaPitch);
                }
            } else if (buffer > 0) buffer -= 1;
        }
    }
}
