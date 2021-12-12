package arctic.ac.data.processor;

import arctic.ac.data.PlayerData;
import eu.salers.salty.event.impl.SaltyPacketInReceiveEvent;
import eu.salers.salty.event.impl.SaltyPacketOutSendEvent;
import lombok.Getter;

public abstract class Processor {

    @Getter
    private final PlayerData data;

    public Processor(PlayerData data) {
        this.data = data;
    }

    public abstract void handleIn(final SaltyPacketInReceiveEvent event);

    public abstract void handleOut(final SaltyPacketOutSendEvent event);
}
