package arctic.ac.check.checks.player.timer;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.server.ServerPositionEvent;

public class TimerA extends Check {

    private long lastTickTime;
    private double balance;
    private double lastRate;
    private double buffer;
    private double buffer3;
    private double buffer2;


    public TimerA(PlayerData data) {
        super(data, "Timer", "A", "player.timer.a", "Checks if player is changing packets rate.", true);
    }

    @Override
    public void handle(Event e) {

        if (e instanceof FlyingEvent) {
            if (data.getInteractData().getLastHitPacket() != 0) {
                if (getMillis(data.getInteractData().getLastHitPacket()) < 110L || data.getInteractData().isHurt())
                    return;
            }
            if (data.getInteractData().getTicksAlive() < 30) return;
            long systemTime = System.currentTimeMillis();
            long lastTimeRate = this.lastTickTime != 0 ? this.lastTickTime : systemTime - 50;
            this.lastTickTime = systemTime;
            balance += 50.0;
            balance -= (systemTime - lastTimeRate);

            if (balance >= 100) {
                fail("bal " + balance);
                balance = 0.0;
            }


            double rate = (systemTime - lastTimeRate);
            double lastRate = this.lastRate;
            this.lastRate = rate;

            double avgRate = Math.abs(rate - lastRate);


            if (rate < 10) return;

            if (buffer2 > 0) buffer2 -= 0.001;
            if (avgRate < 5 && rate > 5) {
                if (rate < 45 || rate > 70) {
                    buffer++;
                    if (buffer > 4) {
                        buffer2++;
                        if (buffer2 > 20) {
                            fail("rate " + rate + " avgRate " + avgRate);
                        }
                    }
                } else {
                    if (buffer > 0) {
                        buffer -= 0.25;
                    }
                }
            } else {
                if (buffer > 0) {
                    buffer -= 0.25;
                }
            }

            debug("balance=" + balance);
        } else if (e instanceof ServerPositionEvent) {

            balance -= 50.0D;

        }
    }

    private long getMillis(long val) {
        return System.currentTimeMillis() - val;
    }
}
