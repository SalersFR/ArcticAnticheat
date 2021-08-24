package polar.ac.check.checks.combat.aim;

import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.RotationEvent;

public class AimF extends Check {

    private double lastDeltaPitch;

    public AimF(PlayerData data) {
        super(data, "Aim","F","combat.aim.f",true);
    }

    @Override
    public void handle(Event e) {
        if(e instanceof RotationEvent) {

            final RotationEvent event = (RotationEvent) e;

            final double deltaPitch = event.getDeltaPitch();
            final double pitch = event.getDeltaPitch();

            final double deltaYaw = event.getDeltaYaw();

            final double lastDeltaPitch = this.lastDeltaPitch;
            this.lastDeltaPitch = deltaPitch;

            final double accel = Math.abs(deltaPitch - lastDeltaPitch);

            final boolean exemptCombat = (System.currentTimeMillis() - data.getInteractionData().getLastHitPacket()) > 250L;

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || deltaYaw < 9.01D;

            debug("accel=" +accel +" deltaPitch="+ deltaPitch +" lastDeltaPitch=" + lastDeltaPitch +" exempt=" + exempt);

            if(!exempt && !exemptCombat) {
                if(accel <= 0.001F) {
                    if(++buffer > 4) {
                        buffer /= 2;
                        fail("accel=" + accel);
                    }
                }else if(buffer > 0) buffer -= 0.5D;
            }

        }
    }
}