package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.utils.MathUtils;

public class AimG extends Check {

    // not entierly from me, a friend helped me - Salers

    private double lastDeltaPitch, lastPitchAtan, result;

    public AimG(PlayerData data) {
        super(data, "Aim", "G", "combat.aim.g", "Checks for invalid GCD.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {

            final RotationEvent event = (RotationEvent) e;

            final double absDeltaPitch = Math.abs(event.getDeltaPitch());
            final double absLastDeltaPitch = Math.abs(this.lastDeltaPitch);

            final double pitch = event.getTo().getPitch();

            this.lastDeltaPitch = absDeltaPitch;

            final double gcd = MathUtils.gcd(0x4000, (absDeltaPitch * MathUtils.EXPANDER), (absLastDeltaPitch * MathUtils.EXPANDER));

            debug("gcd=" + gcd);

            if (data.getCinematicProcessor().getTicksSince() < 10) return;

            if (Math.min(this.lastPitchAtan, Math.atan(pitch)) == this.result && gcd < 0x20000 && gcd > 0 && event.getDeltaYaw() < 105) {
                if (this.buffer < 15) buffer++;

                if (this.buffer > 2)
                    fail("gcd=" + gcd);
            } else this.buffer -= this.buffer > 0 ? 0.05 : 0;


            this.result = Math.min(this.lastPitchAtan, Math.atan(pitch));
            this.lastPitchAtan = Math.atan(pitch);

        }

    }

}

