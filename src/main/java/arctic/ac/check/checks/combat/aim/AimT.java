package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.utils.MathUtils;

public class AimT extends Check {

    private float lastDeltaPitch, lastDeltaYaw;

    public AimT(PlayerData data) {
        super(data, "Aim", "T", "combat.aim.t", "Checks for consistent rotations.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {
            final RotationEvent event = (RotationEvent) e;

            final float deltaYaw = event.getDeltaYaw();
            final float lastDeltaYaw = this.lastDeltaYaw;

            final float deltaPitch = event.getDeltaPitch();
            final float lastDeltaPitch = this.lastDeltaPitch;

            this.lastDeltaPitch = deltaPitch;
            this.lastDeltaYaw = deltaYaw;

            final double gcdYaw = MathUtils.getGcd(deltaYaw, lastDeltaYaw);
            final double gcdPitch = MathUtils.getGcd(deltaPitch, lastDeltaPitch);

            final double consist = Math.abs(gcdYaw - gcdPitch);

            if (consist < 0.005 && (deltaYaw > 2.75f || (deltaPitch != 0.0f && deltaYaw > 1.25f)) && !Double.toString(consist).contains("E") && Math.abs(event.getTo().getPitch()) != 90 && Math.abs(event.getFrom().getPitch()) != 90) {

                buffer += (0.25 + (consist * 50f));
                if (consist == 0.0f) buffer *= 0.1f;

                if (buffer > 1.5)
                    fail("const=" + consist);
            } else if (buffer > 0) buffer -= 0.001;

        }
    }
}
