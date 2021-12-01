package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.utils.MathUtils;

public class AimU extends Check {

    private float lastDeltaPitch;
    private double buffer;

    public AimU(PlayerData data) {
        super(data, "Aim", "U", "combat.aim.u", "Checks for very slow rotations.", true);
    }

    @Override
    public void handle(Event event) {
        if (event instanceof RotationEvent) {
            RotationEvent e = (RotationEvent) event;

            float deltaYaw = e.getDeltaYaw();
            float deltaPitch = e.getDeltaPitch();

            float lastDeltaPitch = this.lastDeltaPitch;
            this.lastDeltaPitch = deltaPitch;

            double gcd = MathUtils.getGcd(deltaPitch, lastDeltaPitch);

            boolean validPitch = true;
            boolean validYaw = true;
            if (deltaPitch == 0 && gcd > 0.8 && deltaYaw > 3 && Math.abs(e.getTo().getPitch()) != 90 && Math.abs(e.getFrom().getPitch()) != 90) validPitch = false;
            if (deltaYaw == 0 && deltaPitch > 0.1 && gcd > 0.4 && Math.abs(e.getTo().getPitch()) != 90 && Math.abs(e.getFrom().getPitch()) != 90) validYaw = false;

            boolean attacking = System.currentTimeMillis() - data.getInteractData().getLastHitPacket() < 50 * 1.5;

            if (validPitch && validYaw) {
                if (buffer > 0) buffer -= 0.05;
            } else {
                buffer++;
                if (buffer > 5 && attacking) {
                    fail("gcd " + gcd + "  buffer  " + buffer);
                }
            }
        }
    }
}
