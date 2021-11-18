package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.utils.MathUtils;

public class AimH extends Check {

    private float lastDeltaYaw, lastDeltaPitch;
    private double lastGCD;

    public AimH(PlayerData data) {
        super(data, "Aim", "H", "combat.aim.h", "Checks for GCD-Fix modules.", true);
    }

    @Override
    public void handle(Event event) {
        if (event instanceof RotationEvent) {
            RotationEvent rotationEvent = (RotationEvent) event;

            final float deltaYaw = rotationEvent.getDeltaYaw();
            final float deltaPitch = rotationEvent.getDeltaPitch();

            // Grab the gcd using an expander.
            final double gcdYaw = MathUtils.getGcd((long) (deltaYaw * MathUtils.EXPANDER), (long) (lastDeltaYaw * MathUtils.EXPANDER));
            final double gcdPitch = MathUtils.getGcd((long) (deltaPitch * MathUtils.EXPANDER), (long) (lastDeltaPitch * MathUtils.EXPANDER));

            final double gcd = gcdYaw / gcdPitch;
            final double diffGCD = Math.abs(gcd - lastGCD);

            debug("gcdDiff=" + diffGCD + " buffer=" + buffer);


            final float pitch = ((RotationEvent) event).getTo().getPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || deltaYaw < 3.5D;
            final boolean exemptCombat = (System.currentTimeMillis() - data.getInteractionData().getLastHitPacket()) > 100L;

            lastDeltaYaw = deltaYaw;
            lastDeltaPitch = deltaPitch;
            lastGCD = gcd;

            if (!exempt && !exemptCombat) {
                if (Double.toString(diffGCD).contains("E")) {
                    if (++buffer > 8)
                        fail("gcdDiff=" + diffGCD);
                } else if (buffer > 0) buffer -= 0.25D;
            }
        }
    }
}
