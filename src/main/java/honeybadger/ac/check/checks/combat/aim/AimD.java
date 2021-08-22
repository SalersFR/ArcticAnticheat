package honeybadger.ac.check.checks.combat.aim;

import honeybadger.ac.check.Check;
import honeybadger.ac.data.PlayerData;
import honeybadger.ac.event.Event;
import honeybadger.ac.event.client.RotationEvent;
import honeybadger.ac.utils.MathUtils;

public class AimD extends Check {

    private float lastDeltaPitch,lastDeltaYaw;

    public AimD(PlayerData data) {
        super(data, "Aim","D","combat.aim.d",true);
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
                if (++buffer > 5) {
                    buffer -= 2.5D;
                    fail("deltaPitch=" + deltaPitch + " lastDeltaPitch=" + lastDeltaPitch + " deltaYaw=" + deltaYaw + " lastDeltaYaw=" + lastDeltaYaw);
                }
            } else if (buffer > 0) buffer--;

        }
    }
}
