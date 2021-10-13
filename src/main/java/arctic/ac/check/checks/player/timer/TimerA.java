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

            if(++balance > 22) {
                if(++buffer > 3)
                    Bukkit.broadcastMessage("§cBALANCECJHEAQZEHJQZIDNQZ=" + balance);
            } else {
                Bukkit.broadcastMessage("§abalance=" + balance);
            }

            Bukkit.getScheduler().runTaskLaterAsynchronously(Arctic.INSTANCE, () -> {
                balance = 0;

            }, 20L);

        }

    }

    private long getMillis(long val) {
        return System.currentTimeMillis() - val;
    }
}
