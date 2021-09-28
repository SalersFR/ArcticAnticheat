package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.MoveEvent;
import arctic.ac.event.client.RotationEvent;

public class AimM extends Check {

    private double lastDeltaYaw,deltaXZ;

    public AimM(PlayerData data) {
        super(data, "Aim", "M", "combat.aim.m", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {

            final RotationEvent event = (RotationEvent) e;

            final double deltaYaw = event.getDeltaYaw();
            final double lastDeltaYaw = this.lastDeltaYaw;

            this.lastDeltaYaw = deltaYaw;

            final boolean exempt = deltaYaw == lastDeltaYaw || deltaXZ < 0.19D;
            final boolean exemptCombat = (System.currentTimeMillis() - data.getInteractionData().getLastHitPacket()) > 100L;

            final double accel = Math.abs(deltaYaw - lastDeltaYaw);

            if (!exempt && !exemptCombat) {
                debug("accel=" + accel);
                if (accel < 0.00401D) {
                    if (++buffer > 3) {
                        fail("accel=" + accel + " dy=" + deltaYaw);
                    }
                } else if (buffer > 0) buffer -= 1.5D;
            }
        } else if(e instanceof MoveEvent) {
            this.deltaXZ = ((MoveEvent) e).getDeltaXZ();
        }

    }
}
