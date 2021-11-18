package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;

public class AimS extends Check {

    //Checks for not smoothed aimBots such as LiquidBounce.
    long lastRotated;
    double buffer;
    public AimS(PlayerData data) {
        super(data, "Aim", "S", "combat.aim.s", "Checks for special rotations.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {
            long now = System.currentTimeMillis();
            long before = lastRotated;
            lastRotated = now;


            double delta = ((RotationEvent) e).getDeltaYaw();
            long diff = now - before;

            debug("diff=" + diff + " delta=" + delta + " buffer=" + buffer);
            if (diff > 250 && diff < 400) {
                if (delta > 3) {
                    buffer++;
                    if (buffer > 3.75) {
                        fail("&9time&f: " + diff + " &9buffer&f: " + buffer);
                    }
                }
            } else if (buffer > 0) buffer -= 0.025;
        }
    }
}
