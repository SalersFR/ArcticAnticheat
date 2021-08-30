package arctic.ac.listener.packet;

import arctic.ac.Arctic;
import arctic.ac.check.Check;
import arctic.ac.data.PlayerData;
import arctic.ac.event.client.*;
import arctic.ac.event.server.ServerPositionEvent;
import arctic.ac.event.server.ServerVelocityEvent;
import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.GameMode;

public class PacketHandler {

    public PacketHandler() {

        final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        for (PacketType types : PacketType.values()) {
            if (types.isSupported()) {
                manager.addPacketListener(new PacketAdapter(Arctic.INSTANCE, types) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {

                        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

                        data.getPacketProcessor().handleReceive(event);
                    }

                    @Override
                    public void onPacketSending(PacketEvent event) {

                        final PlayerData data = Arctic.INSTANCE.getDataManager().getPlayerData(event.getPlayer());

                        data.getPacketProcessor().handleSending(event);
                    }
                });


            }
        }
    }


}
