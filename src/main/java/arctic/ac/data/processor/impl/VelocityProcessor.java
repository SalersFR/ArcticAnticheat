package arctic.ac.data.processor.impl;

import arctic.ac.data.PlayerData;
import arctic.ac.data.processor.Processor;
import eu.salers.salty.event.impl.SaltyPacketInReceiveEvent;
import eu.salers.salty.event.impl.SaltyPacketOutSendEvent;

public class VelocityProcessor extends Processor {

    public VelocityProcessor(PlayerData data) {
        super(data);
    }

    @Override
    public void handleIn(SaltyPacketInReceiveEvent event) {

    }

    @Override
    public void handleOut(SaltyPacketOutSendEvent event) {

    }
}
