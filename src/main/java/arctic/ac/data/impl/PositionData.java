package arctic.ac.data.impl;

import arctic.ac.data.PlayerData;
import com.comphenix.protocol.PacketType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class PositionData {

    private PacketType lastPacket;
    private long lastFlying;
    private Player player;
    private PlayerData data;

    public PositionData(PlayerData data) {
        this.player = data.getPlayer();
        this.data = data;
    }
}
