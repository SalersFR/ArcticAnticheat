package arctic.ac.listener.packet;

import arctic.ac.Arctic;
import arctic.ac.data.PlayerData;
import eu.salers.salty.event.impl.SaltyPacketInReceiveEvent;
import eu.salers.salty.event.impl.SaltyPacketOutSendEvent;
import eu.salers.salty.event.listener.SaltyPacketListener;

public class PacketHandler extends SaltyPacketListener {


    @Override
    public void onPacketInReceive(SaltyPacketInReceiveEvent event) {
        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

        if(data == null) return;

        Arctic.INSTANCE.getDataThread().execute(() -> data.handleReceive(event));


    }

    @Override
    public void onPacketOutSend(SaltyPacketOutSendEvent event) {
        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

        if(data == null) return;

        Arctic.INSTANCE.getDataThread().execute(() -> data.handleSending(event));
    }
}
