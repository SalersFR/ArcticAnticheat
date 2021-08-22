package honeybadger.ac.check.checks.combat.aim;

import honeybadger.ac.check.Check;
import honeybadger.ac.data.PlayerData;
import honeybadger.ac.event.Event;
import honeybadger.ac.event.client.RotationEvent;
import honeybadger.ac.utils.MathUtils;

public class AimA extends Check {

    private float lastDeltaYaw, lastDeltaPitch;

    public AimA(PlayerData data) {
        super(data, "Aim", "A", "combat.aim.a", true);
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

            if (deltaYaw > 2.0 && lastDeltaYaw > 2.0) {
                debug("deltaYaw=" + MathUtils.floor(deltaYaw) + " lastDeltaYaw=" + MathUtils.floor(lastDeltaYaw) + " sens=" + MathUtils.getSensitivity(deltaPitch, lastDeltaPitch) + "%");

                if (MathUtils.floor(deltaYaw) > 185 && MathUtils.floor(lastDeltaYaw) > 185) {
                    if (MathUtils.getSensitivity(deltaPitch, lastDeltaPitch) > 200
                            && !(MathUtils.getSensitivity(deltaPitch, lastDeltaPitch) < 10)) {
                        if (++buffer > 1.2D) {
                            fail("deltaYaw=" + MathUtils.floor(deltaYaw) + " lastDeltaYaw=" + MathUtils.floor(lastDeltaYaw)
                                    + " sens=" + MathUtils.getSensitivity(deltaPitch, lastDeltaPitch) + "%");

                        }

                    } else if (buffer > 0) buffer -= 0.12D;
                }
            }
        }
    }
}
