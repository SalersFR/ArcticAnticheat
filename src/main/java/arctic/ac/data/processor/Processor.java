package arctic.ac.data.processor;

import arctic.ac.data.PlayerData;
import eu.salers.salty.event.impl.SaltyPacketInReceiveEvent;
import eu.salers.salty.event.impl.SaltyPacketOutSendEvent;
import eu.salers.salty.packet.type.PacketType;
import lombok.Getter;

public abstract class Processor {

    @Getter
    private final PlayerData data;

    public Processor(PlayerData data) {
        this.data = data;
    }

    public abstract void handleIn(final SaltyPacketInReceiveEvent event);

    public abstract void handleOut(final SaltyPacketOutSendEvent event);

    protected boolean isFlyingPacket(final PacketType type) {
        return type == PacketType.IN_FLYING || type == PacketType.IN_POSITION_LOOK || type == PacketType.IN_POSITION || type == PacketType.IN_LOOK;
    }
}
