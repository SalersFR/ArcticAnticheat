package dev.arctic.anticheat.packet.event.events;


import dev.arctic.anticheat.packet.Packet;
import dev.arctic.anticheat.packet.event.PacketEvent;
import org.bukkit.entity.Player;

public class PacketSendEvent extends PacketEvent {
    public PacketSendEvent(Player player, Packet packet, long time) {
        super(player, packet, time);
    }

    /**
     * @author Salers
     **/


}
