package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;

public class AimK extends Check {


    public AimK(PlayerData data) {
        super(data, "Aim", "K", "combat.aim.k", true);
    }

    @Override
    public void handle(Event e) {

        if (e instanceof RotationEvent) {


            final RotationEvent event = (RotationEvent) e;

            final float deltaYaw = event.getDeltaYaw();
            final float deltaPitch = event.getDeltaPitch();


            final double pitch = event.getTo().getPitch();

            final boolean exempt = !(pitch < 82.5F && pitch > -82.5F) || deltaYaw == 0.0 || deltaPitch == 0.0;


            if (deltaPitch % 1 == 0 && !exempt) {
                if (buffer < 12) buffer++;

                if (buffer > 5) {
                    fail("deltaPitch=" + deltaPitch);
                }

            } else if (buffer > 0) buffer -= 0.25D;

            if (deltaYaw % 1 == 0 && !exempt) {
                if (buffer < 12) buffer++;

                if (buffer > 5) {
                    fail("deltaYaw=" + deltaYaw);
                }

            } else if (buffer > 0) buffer -= 0.25D;


        }
    }
}
