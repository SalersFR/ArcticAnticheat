package arctic.ac.check.checks.player.badpackets;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.FlyingEvent;
import arctic.ac.event.client.PacketEvent;
import com.comphenix.protocol.PacketType;

public class BadPacketsD extends Check {



    public BadPacketsD(PlayerData data) {
        super(data, "BadPackets", "D", "player.badpackets.d", false);
    }

    @Override
    public void handle(Event e) {
        if(e instanceof FlyingEvent) {
            if(buffer != 0) buffer = 0;
        } else if(e instanceof PacketEvent) {
            final PacketEvent event = (PacketEvent) e;
            if(event.getPacketType() == PacketType.Play.Client.WINDOW_CLICK) {
                if(data.getInteractionData().isSprinting() || data.getInteractionData().isSneaking()) {
                    if(++buffer > 1)
                        fail();
                }
            }
        }
    }
}
