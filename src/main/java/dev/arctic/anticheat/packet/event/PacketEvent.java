package dev.arctic.anticheat.packet.event;

import dev.arctic.anticheat.packet.Packet;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
public class PacketEvent {

    private final Player player;
    private final Packet packet;
    private final long time;


}
