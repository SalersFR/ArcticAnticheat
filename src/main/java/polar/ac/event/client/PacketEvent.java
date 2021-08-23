package polar.ac.event.client;

import com.comphenix.protocol.PacketType;
import lombok.Getter;
import polar.ac.event.Event;

@Getter
public class PacketEvent extends Event {

    private PacketType packetType;

    public PacketEvent(PacketType packetType) {
        this.packetType = packetType;
    }
}
