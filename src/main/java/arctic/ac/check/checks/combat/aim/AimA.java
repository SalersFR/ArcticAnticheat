package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.utils.MathUtils;

public class AimA extends Check {

    private float lastDeltaPitch;

    public AimA(PlayerData data) {
        super(data, "Aim", "A", "combat.aim.a", "Checks for invalid yaw sensitivity.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {
            RotationEvent event = (RotationEvent) e;

            float deltaYaw = event.getDeltaYaw();

            float deltaPitch = event.getDeltaPitch();
            float lastDeltaPitch = this.lastDeltaPitch;
            this.lastDeltaPitch = deltaPitch;

            final float sensitivity = (float) MathUtils.getSensitivity(deltaPitch, lastDeltaPitch);


            if (sensitivity != -66.66 && sensitivity <= -1 && deltaYaw > 6.25D && data.getCinematicProcessor().getTicksSince() >
                    35 && data.getCinematicProcessor().getLastAccelYaw() < 1.5 && deltaPitch != 0.0f) {
                debug("sensitivity=" + sensitivity);
                if (++buffer > 8)
                    fail("sensitivity=" + sensitivity);
            } else if (buffer > 0) buffer -= 0.75D;


        }
    }

}

