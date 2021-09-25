package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.utils.MathUtils;

public class AimI extends Check {

    private float lastDeltaPitch;

    public AimI(PlayerData data) {
        super(data, "Aim", "I", "combat.aim.i", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {

            final RotationEvent event = (RotationEvent) e;


            float deltaYaw = event.getDeltaYaw();


            float deltaPitch = event.getDeltaPitch();
            float lastDeltaPitch = this.lastDeltaPitch;
            this.lastDeltaPitch = deltaPitch;

            final double gcd = MathUtils.gcd(0x4000,
                    (Math.abs(deltaPitch) * MathUtils.EXPANDER), (Math.abs(lastDeltaPitch) * MathUtils.EXPANDER));
            final float pitch = event.getTo().getPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || deltaYaw < 5.0 || deltaPitch == 0.0 ||
                    lastDeltaPitch == 0.0 || data.getCinematicProcessor().getTicksSince() < 10;
            final boolean exemptCombat = (System.currentTimeMillis() - data.getInteractionData().getLastHitPacket()) > 100L;

            debug("gcd=" + gcd + " deltaYaw=" + deltaYaw + " exempt=" + exempt);


            if (!exemptCombat && !exempt) {
                if (gcd < 131072L) {
                    if (buffer < 15) buffer++;

                    if (buffer > 8)
                        fail("gcd=" + gcd);

                } else if (buffer >= 1.5D) buffer -= 1.5D;
            }
        }
    }
}
