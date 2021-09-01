package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;

public class AimL extends Check {

    /**
     * @author DerRedstoner
     * @date 31/08/2012
     */

    public AimL(PlayerData data) {
        super(data, "Aim","L","combat.aim.k",true);
    }

    @Override
    public void handle(Event e) {
        if(e instanceof RotationEvent) {

            final RotationEvent event = (RotationEvent) e;

            final double deltaYaw = Math.abs(event.getDeltaYaw());
            final double deltaPitch = Math.abs(event.getDeltaPitch());

            final float pitch = event.getTo().getPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F);

            debug("deltaYaw=" + deltaYaw +" deltaPitch=" + deltaPitch);

            if(exempt) return;

            if(Double.toString(deltaPitch).contains("E") || Double.toString(deltaYaw).contains("E")) {
                if(++buffer > 2) {
                    fail("too small delta");
                }
            } else if(buffer > 0) buffer -= 0.025D;

        }
    }
}
