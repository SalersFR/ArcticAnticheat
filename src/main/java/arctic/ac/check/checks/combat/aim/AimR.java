package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.utils.MathUtils;

public class AimR extends Check {

    private float lastDeltaYaw, lastDeltaPitch;
    private double avgGcdYaw;

    public AimR(PlayerData data) {
        super(data, "Aim", "R", "combat.aim.r", "Checks for special rotations.", true);
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


            final double gcdPitch = MathUtils.gcd(0x4000, (Math.abs(deltaPitch)), Math.abs(lastDeltaPitch));
            final double gcdYaw = MathUtils.gcd(0x4000, (Math.abs(deltaYaw)), Math.abs(lastDeltaYaw));

            avgGcdYaw = ((avgGcdYaw * 14) + gcdYaw) / 15;
            final double gcdYawDev = Math.abs(gcdYaw - avgGcdYaw);

            debug("buffer=" + buffer + " dev=" + gcdYawDev);

            if (deltaYaw > 1.25 && deltaPitch > 0.01f && gcdYawDev < 0.09 && gcdYawDev > 0.01D && gcdPitch < 0.415D) {
                if (++buffer > 4.275)
                    fail("gcdYaw=" + gcdYawDev);
            } else if (buffer > 0) buffer -= 0.0125D;


        }

    }
}
