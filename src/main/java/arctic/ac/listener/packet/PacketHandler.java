package arctic.ac.listener.packet;

import arctic.ac.Arctic;
import arctic.ac.data.PlayerData;
import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;

public class PacketHandler extends PacketListenerDynamic {

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

        if (data == null) return;

        data.getPacketProcessor().handleReceive(event);
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

        if (data == null) return;

        data.getPacketProcessor().handleSending(event);
    }


}
