package arctic.ac.event.client;

import arctic.ac.event.Event;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import lombok.Getter;

@Getter
public class PacketReceiveEvent extends Event {

    private NMSPacket nmsPacket;
    private byte packetType;

    public PacketReceiveEvent(final PacketPlayReceiveEvent event) {
        this.nmsPacket = event.getNMSPacket();
        this.packetType = event.getPacketId();
    }
}
