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
        super(data, "Aim", "H", "combat.aim.h", "Checks for invalid pitch rotations.", true);
    }

    @Override
    public void handle(Event event) {
        if (event instanceof RotationEvent) {
            RotationEvent rotationEvent = (RotationEvent) event;

            final float deltaYaw = rotationEvent.getDeltaYaw();
            final float deltaPitch = rotationEvent.getDeltaPitch();

            final float pitch = ((RotationEvent) event).getTo().getPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || deltaYaw < 3.5D;
            final boolean exemptCombat = (System.currentTimeMillis() - data.getInteractionData().getLastHitPacket()) > 100L;

            if(Math.abs(deltaPitch) > 15.25 && Math.abs(deltaYaw) <= 3.0E-5 && !exempt && !exemptCombat) {
                if(++buffer > 4.75)
                    fail("deltaPitch=" + deltaPitch + " deltaYaw=" + deltaYaw);
            } else if(buffer > 0) buffer -= 0.1D;

            lastDeltaYaw = deltaYaw;
            lastDeltaPitch = deltaPitch;

        }
    }
}
