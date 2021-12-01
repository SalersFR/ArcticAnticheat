package arctic.ac.check.checks.player.timer;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.PacketReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;


public class TimerB extends Check {


    private int packets;


    public TimerB(PlayerData data) {
        super(data, "Timer", "B", "player.timer.b", "Checks for speeding up packets rate.", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof PacketReceiveEvent) {
            final PacketReceiveEvent event = (PacketReceiveEvent) e;

            if (event.getPacketType() == PacketType.Play.Client.KEEP_ALIVE) {
                final int diff = Math.abs(41 - packets);
                if (diff >= 7 && data.getInteractData().getTicksAlive() > 120 && diff != 41) {
                    buffer += (diff * 0.1F);
                    if (buffer > 3) {
                        //fail("diff=" + diff);
                    }
                } else if (buffer > 0) buffer -= 0.2D;

                debug("diff=" + diff + " packets=" + packets);
                this.packets = 0;

            }

        } else if (e instanceof FlyingEvent) {
            this.packets++;


        }


    }
}
