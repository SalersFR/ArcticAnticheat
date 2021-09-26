package arctic.ac.check.checks.player.badpackets;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.PacketEvent;
import com.comphenix.protocol.PacketType;

public class BadPacketsF extends Check {

    private int ticksSince;


    public BadPacketsF(PlayerData data) {
        super(data, "BadPackets", "F", "player.badpackets.f", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof PacketEvent) {

            final PacketEvent event = (PacketEvent) e;
            final PacketType type = event.getPacketType();

            if (type == PacketType.Play.Client.KEEP_ALIVE) {
                this.ticksSince = 0;
            }

        } else if (e instanceof FlyingEvent) {

            debug("ticksSince=" + ticksSince);

            if (++this.ticksSince > 79) {
                if (++buffer > 2)
                    fail("ticks=" + ticksSince);

            } else if (buffer > 0) buffer = 0;

        }

    }
}
