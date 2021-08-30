package arctic.ac.check.checks.movement.nofall;

import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.Event;
import arctic.ac.event.client.PacketEvent;
import com.comphenix.protocol.PacketType;

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
