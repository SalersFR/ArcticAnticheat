package polar.ac.check.checks.movement.nofall;

import com.comphenix.protocol.PacketType;
import polar.ac.check.Check;
import polar.ac.data.PlayerData;
import polar.ac.event.Event;
import polar.ac.event.client.PacketEvent;

public class NoFallA extends Check {

    /*
    Packet NoFall check
     */

    public NoFallA(PlayerData data) {
        super(data, "NoFall", "A", "movement.nofall.a", true);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof PacketEvent) {
            PacketEvent event = (PacketEvent) e;

            if (event.getPacketType().equals(PacketType.Play.Client.FLYING)) {
                if (data.getPosData().getLastPacket().equals(PacketType.Play.Client.POSITION)
                        && data.getBukkitPlayerFromUUID().getFallDistance() > 0.1) {
                    if (++buffer >= 3) {
                        fail("currentPacket=FLYING" + " lastPacket=POSITION");
                    }
                } else if (buffer > 0) buffer -= 0.25;
            }
        }
    }
}
