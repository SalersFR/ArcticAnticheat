package dev.arctic.anticheat.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import dev.arctic.anticheat.Arctic;
import dev.arctic.anticheat.ArcticPlugin;
import dev.arctic.anticheat.data.PlayerData;
import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.packet.event.events.PacketReceiveEvent;
import dev.arctic.anticheat.packet.event.events.PacketSendEvent;
import org.bukkit.entity.Player;

public class PacketListener {

    public PacketListener(final ArcticPlugin plugin) {
        for (PacketType packetType : PacketType.values()) {
            if (packetType.isSupported()) {

                ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGH, packetType) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        onPacketReceive(event.getPlayer(), event.getPacket());
                    }

                    @Override
                    public void onPacketSending(PacketEvent event) {
                        onPacketSend(event.getPlayer(), event.getPacket());
                    }
                });

            }
        }

    }

    public void onPacketReceive(Player player, PacketContainer packet) {
        PlayerData data = Arctic.getInstance().getPlayerDataManager().getPlayerData(player);
        if (data == null)
            return;

        Arctic.getInstance().getDataService().execute(() ->
                data.handle(new PacketReceiveEvent(player, new Packet(packet), System.currentTimeMillis())));


    }

    public void onPacketSend(Player player, PacketContainer packet) {
        PlayerData data = Arctic.getInstance().getPlayerDataManager().getPlayerData(player);
        if (data == null)
            return;

        Arctic.getInstance().getDataService().execute(() ->
                data.handle(new PacketSendEvent(player, new Packet(packet), System.currentTimeMillis())));
    }


}
