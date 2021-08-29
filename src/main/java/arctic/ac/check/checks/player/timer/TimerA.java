package arctic.ac.check.checks.player.timer;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.server.ServerPositionEvent;

public class TimerA extends Check {
    private long lastTickTime;
    private double balance;
    public TimerA(PlayerData data) {
        super(data, "Timer", "A", "player.timer.a", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof FlyingEvent) {
            if (data.getInteractData().getLastHitPacket() != 0) {
                if (getMillis(data.getInteractData().getLastHitPacket()) < 110L) return;
            }
            long systemTime = System.currentTimeMillis();
            long lastTimeRate = this.lastTickTime != 0 ? this.lastTickTime : systemTime - 50;
            this.lastTickTime = systemTime;
            balance += 50.0;
            balance -= (systemTime - lastTimeRate);

            if (balance >= 30.0) {
                if (++buffer > 6) {
                    fail("balance=" + balance);
                    balance = 0.0D;
                }
            } else {
                buffer -= (buffer > 0) ? 2 : 0;
            }

            debug("balance=" + balance);
        } else if (e instanceof ServerPositionEvent) {
            balance -= 50.D;

        }
    }

    private long getMillis(long val) {
        return System.currentTimeMillis() - val;
    }
}