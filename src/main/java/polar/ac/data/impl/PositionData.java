package polar.ac.data.impl;

import com.comphenix.protocol.PacketType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import polar.ac.data.PlayerData;

@Getter
@Setter
public class PositionData {

    private PacketType lastPacket;
    private Player player;
    private PlayerData data;

    public PositionData(PlayerData data) {
        this.player = data.getPlayer();
        this.data = data;
    }
}
