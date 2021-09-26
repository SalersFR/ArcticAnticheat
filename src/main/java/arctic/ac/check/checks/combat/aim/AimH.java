package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.utils.MathUtils;

public class AimH extends Check {

    private float lastDeltaYaw, lastDeltaPitch;

    public AimH(PlayerData data) {
        super(data, "Aim", "H", "combat.aim.h", true);
    }

    @Override
    public void handle(Event event) {
        if (event instanceof RotationEvent) {
            RotationEvent rotationEvent = (RotationEvent) event;
            /**
             CREDITS FREQUENCY
             **/


            // Get the deltas from the rotation update
            float deltaYaw = rotationEvent.getDeltaYaw();
            float deltaPitch = rotationEvent.getDeltaPitch();

            // Grab the gcd using an expander.
            double divisorYaw = MathUtils.getGcd((long) (deltaYaw * MathUtils.EXPANDER), (long) (lastDeltaYaw * MathUtils.EXPANDER));
            double divisorPitch = MathUtils.getGcd((long) (deltaPitch * MathUtils.EXPANDER), (long) (lastDeltaPitch * MathUtils.EXPANDER));

            // Get the constant for both rotation updates by dividing by the expander
            double constantYaw = divisorYaw / MathUtils.EXPANDER;
            double constantPitch = divisorPitch / MathUtils.EXPANDER;

            // Get the estimated mouse delta from the constant
            double deltaX = deltaYaw / constantYaw;
            double deltaY = deltaPitch / constantPitch;

            // Get the estimated mouse delta from the old rotations using the new constant
            double lastDeltaX = lastDeltaYaw / constantYaw;
            double lastDeltaY = lastDeltaPitch / constantPitch;

            lastDeltaYaw = deltaYaw;
            lastDeltaPitch = deltaPitch;
        }
    }
}
