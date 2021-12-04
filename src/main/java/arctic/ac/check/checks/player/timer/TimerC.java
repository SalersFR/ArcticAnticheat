package arctic.ac.check.checks.player.timer;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;

public class TimerC extends Check {

    private long lastFlying;
    private int avgDiff;

    public TimerC(PlayerData data) {
        super(data, "Timer", "C", "player.timer.c", "Checks for speeding up packets rate.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof FlyingEvent) {

            final int diff = (int) (System.currentTimeMillis() - lastFlying);

            avgDiff = ((avgDiff * 14) + diff) / 15;

            debug("diff=" + diff + "avg=" + avgDiff + " buffer=" + buffer);

            final int threshold = 38 - ((data.getNetworkProcessor().getKeepAlivePing()) / 25);

            if(avgDiff < -100) avgDiff = 41;

            if(avgDiff <= threshold && data.getInteractData().getTicksSinceTeleport() > 200) {
                if((buffer += avgDiff) > 625) {
                    fail("avgDiff=" + avgDiff);
                }

            } else if(buffer >= 12) buffer -= 15;

            if(buffer < 0) buffer = 0;

            this.lastFlying = System.currentTimeMillis();


        }

    }
}
