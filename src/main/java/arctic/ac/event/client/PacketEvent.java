package arctic.ac.event.client;

import arctic.ac.event.Event;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import lombok.Getter;

@Getter
public class PacketEvent extends Event {

    private PacketType packetType;
    private PacketContainer container;

    public PacketEvent(com.comphenix.protocol.events.PacketEvent packetEvent) {
        this.packetType = packetEvent.getPacketType();
        this.container = packetEvent.getPacket();
    }
}
