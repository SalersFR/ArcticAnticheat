package arctic.ac.check.checks.player.timer;

import arctic.ac.Arctic;
import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import org.bukkit.Bukkit;

public class TimerA extends Check {

    private double balance;

    public TimerA(PlayerData data) {
        super(data, "Timer", "A", "player.timer.a", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof FlyingEvent) {

            this.balance++;

            final boolean exempt = data.getInteractData().getTicksAlive() < 50;

            debug("exempt=" + exempt + " balance=" + balance + " buffer=" + buffer);

            if (!exempt && balance >= 2) {
                if (++buffer > 7) {
                    buffer -= 1.25D;

                    fail("balance=" + balance);
                }

            } else if (buffer > 0) buffer -= 0.25D;

            Bukkit.getScheduler().runTaskLaterAsynchronously(Arctic.INSTANCE, () -> {
                balance = 0;

            }, 20L);

        }

    }

    private long getMillis(long val) {
        return System.currentTimeMillis() - val;
    }
}
