package honeybadger.ac.check.checks.combat.aim;

import honeybadger.ac.check.Check;
import honeybadger.ac.data.PlayerData;
import honeybadger.ac.event.Event;
import honeybadger.ac.event.client.RotationEvent;
import honeybadger.ac.utils.MathUtils;

public class AimB extends Check {

    private float lastDeltaPitch;

    public AimB(PlayerData data) {
        super(data, "Aim", "B", "combat.aim.b", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {
            RotationEvent event = (RotationEvent) e;

            float deltaYaw = event.getDeltaYaw();


            float deltaPitch = event.getDeltaPitch();
            float lastDeltaPitch = this.lastDeltaPitch;
            this.lastDeltaPitch = deltaPitch;

            final double gcd = MathUtils.getGcd(deltaPitch, lastDeltaPitch);
            final float pitch = event.getTo().getPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || deltaYaw < 1.2D;

            debug("gcd=" + gcd + " deltaYaw=" + deltaYaw + " exempt=" + exempt);

            if (gcd <= 0.0 && !exempt) {
                if (++buffer > 1.5) {
                    fail("gcd=" + gcd);
                } else if (buffer > 0) buffer -= 0.25;
            }
        }
    }
}
