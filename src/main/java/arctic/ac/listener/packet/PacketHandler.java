package arctic.ac.listener.packet;

import arctic.ac.Arctic;
import arctic.ac.data.PlayerData;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PacketHandler {

    public PacketHandler() {

        final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        for (PacketType types : PacketType.values()) {
            if (types.isSupported()) {
                manager.addPacketListener(new PacketAdapter(Arctic.INSTANCE, types) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {

                        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

                        if (data == null) return;

                        data.getPacketProcessor().handleReceive(event);
                    }

                    @Override
                    public void onPacketSending(PacketEvent event) {

                        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

                        if (data == null) return;

                        data.getPacketProcessor().handleSending(event);
                    }
                });


            }
        }
    }


}
