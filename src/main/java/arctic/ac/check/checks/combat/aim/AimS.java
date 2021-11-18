package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.RotationEvent;
import org.bukkit.Bukkit;

public class AimS extends Check {

    public AimS(PlayerData data) {
        super(data, "Aim", "S", "combat.aim.s", "Checks for special rotations.", true);
    }
    //Checks for not smoothed aimBots such as LiquidBounce.
    long lastRotated;
    double buffer;

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {
            long now = System.currentTimeMillis();
            long before = lastRotated;
            lastRotated = now;

            double delta = ((RotationEvent) e).getDeltaYaw();
            long diff = now - before;
            if (diff > 250 && diff < 400) {
                if (delta > 3) {
                    buffer++;
                    if (buffer > 4) {
                        fail("&9time&f: " + diff + " &9buffer&f: " + buffer);
                    }
                }
            } else if (buffer > 0) buffer-=0.05;
        }
    }
}
