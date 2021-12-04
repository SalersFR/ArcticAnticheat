package arctic.ac.check.checks.player.timer;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.PacketReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;


public class TimerB extends Check {


    private long lastFlying;
    private int avgDiff;



    public TimerB(PlayerData data) {
        super(data, "Timer", "B", "player.timer.b", "Checks for slowing down packets rate.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof FlyingEvent) {

            final int diff = (int) getMillis(lastFlying);

            avgDiff = ((avgDiff * 14) + diff) / 15;

            debug("diff=" + diff + "avg=" + avgDiff + " buffer=" + buffer);

            final int threshold = 50 + ((data.getNetworkProcessor().getKeepAlivePing()) / 20);

            if(avgDiff >= threshold && data.getInteractData().getTicksSinceTeleport() > 200) {
                if((buffer += avgDiff) > 725) {
                    fail("avgDiff=" + avgDiff);
                }

            } else if(buffer >= 12) buffer -= 15;

            if(buffer < 0) buffer = 0;

            this.lastFlying = System.currentTimeMillis();


        }


    }

    private long getMillis(long val) {
        return System.currentTimeMillis() - val;
    }
}
