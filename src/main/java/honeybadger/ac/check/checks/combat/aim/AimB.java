package honeybadger.ac.check.checks.combat.aim;

import honeybadger.ac.check.Check;
import honeybadger.ac.data.PlayerData;
import honeybadger.ac.event.Event;
import honeybadger.ac.event.client.RotationEvent;
import honeybadger.ac.utils.MathUtils;

public class AimB extends Check {

    private float lastDeltaYaw, lastDeltaPitch;

    public AimB(PlayerData data) {
        super(data, "Aim", "B", "combat.aim.b", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {
            RotationEvent event = (RotationEvent) e;

            float deltaYaw = event.getDeltaYaw();
            float lastDeltaYaw = this.lastDeltaYaw;
            this.lastDeltaYaw = deltaYaw;

            float deltaPitch = event.getDeltaPitch();
            float lastDeltaPitch = this.lastDeltaPitch;
            this.lastDeltaPitch = deltaPitch;

            final double gcd = MathUtils.getGcd(deltaPitch, lastDeltaPitch);

            debug("gcd=" + gcd + " deltaYaw=" + deltaYaw);

            if (gcd <= 0.0 && deltaYaw > 1.2d) {
                if (++buffer > 7.0) {
                    fail("gcd=" + gcd);
                } else if (buffer > 0) buffer -= 0.25;
            }
        }
    }
}
