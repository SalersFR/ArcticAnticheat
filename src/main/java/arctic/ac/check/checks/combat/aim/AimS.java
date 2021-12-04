package arctic.ac.check.checks.combat.aim;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.RotationEvent;
import arctic.ac.utils.MathUtils;
import io.github.retrooper.packetevents.utils.server.ServerUtils;
import org.bukkit.Bukkit;

public class AimS extends Check {

    //Checks for not smoothed aimBots such as LiquidBounce.
    long lastRotated;
    double buffer;
    double speed;
    double buffer2;
    double lastDelta;

    public AimS(PlayerData data) {
        super(data, "Aim", "S", "combat.aim.s", "Checks for robot-like rotations.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof RotationEvent) {
            long now = System.currentTimeMillis();
            long before = lastRotated;
            lastRotated = now;

            double delta = ((RotationEvent) e).getDeltaYaw();
            double lastDelta = this.lastDelta;
            this.lastDelta = delta;

            double gcd = MathUtils.getGcd(delta,lastDelta) * 100;
            speed = ((speed * 9) + gcd) / 10;

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

            if (diff > 90 && diff < 400 && gcd < 100 && speed > 10) {
                buffer2++;
                if (buffer2 > 3) {
                    fail("buffer " + buffer2 + " gcd " + gcd + " diff " + diff +" speed " + speed);
                }
            } else if (buffer2 > 0) buffer2-=0.025;
        }
    }
}
