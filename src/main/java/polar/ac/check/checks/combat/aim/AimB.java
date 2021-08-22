package polar.ac.check.checks.combat.aim;

import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.RotationEvent;
import polar.ac.utils.MathUtils;

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

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || deltaYaw < 3.5D;
            final boolean exemptCombat = (System.currentTimeMillis() - data.getInteractionData().getLastHitPacket()) < 20L;

            debug("gcd=" + gcd + " deltaYaw=" + deltaYaw + " exempt=" + exempt);

            if (gcd <= 0.0 && !exempt && !exemptCombat) {
                if (++buffer > 1.5) {
                    fail("gcd=" + gcd);
                } else if (buffer > 0) buffer -= 0.5;
            }
        }
    }
}
