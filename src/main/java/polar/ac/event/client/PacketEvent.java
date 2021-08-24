package polar.ac.event.client;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import lombok.Getter;
import polar.ac.event.Event;

@Getter
public class PacketEvent extends Event {

    private PacketType packetType;
    private PacketContainer container;

    public PacketEvent(com.comphenix.protocol.events.PacketEvent packetEvent) {
        this.packetType = packetEvent.getPacketType();
        this.container = packetEvent.getPacket();
    }
}
